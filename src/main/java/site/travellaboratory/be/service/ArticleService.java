package site.travellaboratory.be.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.controller.article.dto.ArticleDeleteResponse;
import site.travellaboratory.be.controller.article.dto.ArticleRegisterRequest;
import site.travellaboratory.be.controller.article.dto.ArticleRegisterResponse;
import site.travellaboratory.be.controller.article.dto.ArticleResponse;
import site.travellaboratory.be.controller.article.dto.ArticleTotalResponse;
import site.travellaboratory.be.controller.article.dto.ArticleUpdateRequest;
import site.travellaboratory.be.controller.article.dto.ArticleUpdateResponse;
import site.travellaboratory.be.domain.article.Article;
import site.travellaboratory.be.domain.article.ArticleRepository;
import site.travellaboratory.be.domain.article.ArticleStatus;
import site.travellaboratory.be.domain.bookmark.BookmarkRepository;
import site.travellaboratory.be.domain.bookmark.BookmarkStatus;
import site.travellaboratory.be.domain.user.UserRepository;
import site.travellaboratory.be.domain.user.entity.User;
import site.travellaboratory.be.domain.user.entity.UserStatus;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final BookmarkRepository bookmarkRepository;

    //내 초기 여행 계획 저장
    @Transactional
    public ArticleRegisterResponse saveArticle(final Long userId, final ArticleRegisterRequest articleRegisterRequest) {
        final User user = userRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND,
                        HttpStatus.NOT_FOUND));

        final Article article = Article.of(user, articleRegisterRequest);
        articleRepository.save(article);
        return ArticleRegisterResponse.from(article.getId());
    }

    // 내 초기 여행 계획 전체 조회
    @Transactional
    public Page<ArticleTotalResponse> findByUserArticles(final Long loginId, final Long userId, Pageable pageable) {
        // 사용자 조회
        User user = userRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        // 로그인한 사용자가 이 아티클을 작성하였는지 (수정 가능한 지) 확인
        boolean isEditable = user.getId().equals(loginId);

        // 사용자가 작성한 아티클 또는 다른 사용자의 공개된 아티클 목록 조회
        Page<Article> articlesPage;
        if (isEditable) {
            articlesPage = articleRepository.findByUserAndStatusIn(user,
                            List.of(ArticleStatus.ACTIVE, ArticleStatus.PRIVATE), pageable)
                    .orElseThrow(() -> new BeApplicationException(ErrorCodes.ARTICLE_NOT_FOUND, HttpStatus.NOT_FOUND));
        } else {
            articlesPage = articleRepository.findByUserAndStatusIn(user,
                            List.of(ArticleStatus.ACTIVE), pageable)
                    .orElseThrow(() -> new BeApplicationException(ErrorCodes.ARTICLE_NOT_FOUND, HttpStatus.NOT_FOUND));
        }

        // 각 아티클의 북마크 수 및 북마크 상태 확인하여 ArticleResponse로 변환
        List<ArticleTotalResponse> articleResponses = articlesPage.getContent().stream()
                .map(article -> {
                    Long bookmarkCount = bookmarkRepository.countByArticleIdAndStatus(article.getId(),
                            BookmarkStatus.ACTIVE);
                    boolean isBookmarked = bookmarkRepository.existsByUserIdAndArticleIdAndStatus(loginId,
                            article.getId(), BookmarkStatus.ACTIVE);
                    return ArticleTotalResponse.of(article, bookmarkCount, isBookmarked, isEditable);
                })
                .collect(Collectors.toList());

        // ArticleResponseWithEditable 리스트와 페이지네이션 정보로 Page<ArticleResponseWithEditable> 객체 생성하여 반환
        return new PageImpl<>(articleResponses, pageable, articlesPage.getTotalElements());
    }

    // 초기 여행 계획 한개 조회
    @Transactional
    public ArticleResponse findByArticle(final Long loginId, final Long articleId) {
        // 유효하지 않은 아티클 조회 할 경우
        final Article article = articleRepository.findByIdAndStatusIn(articleId,
                        List.of(ArticleStatus.ACTIVE, ArticleStatus.PRIVATE))
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.ARTICLE_NOT_FOUND, HttpStatus.NOT_FOUND));

        final Long bookmarkCount = bookmarkRepository.countByArticleIdAndStatus(articleId, BookmarkStatus.ACTIVE);

        boolean isBookmarked = bookmarkRepository.existsByUserIdAndArticleIdAndStatus(loginId, articleId,
                BookmarkStatus.ACTIVE);

//        if (!article.getUser().getId().equals(loginId)) {
//            throw new BeApplicationException(ErrorCodes.ARTICLE_READ_DETAIL_NOT_AUTHORIZATION, HttpStatus.UNAUTHORIZED);
//        }

        return ArticleResponse.of(article, bookmarkCount, isBookmarked);
    }

    // 내 초기 여행 계획 수정
    @Transactional
    public ArticleUpdateResponse updateArticle(
            final ArticleUpdateRequest articleUpdateRequest,
            final Long userId,
            final Long articleId
    ) {
        final Article article = articleRepository.findByIdAndStatusIn(articleId,
                        List.of(ArticleStatus.ACTIVE, ArticleStatus.PRIVATE))
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.ARTICLE_NOT_FOUND, HttpStatus.NOT_FOUND));

        if (!article.getUser().getId().equals(userId)) {
            throw new BeApplicationException(ErrorCodes.ARTICLE_UPDATE_NOT_USER, HttpStatus.UNAUTHORIZED);
        }

        article.update(articleUpdateRequest);
        return ArticleUpdateResponse.from(article);
    }

    // 아티클 검색
    @Transactional
    public Page<ArticleTotalResponse> searchArticlesByKeyWord(
            final String keyword,
            final Pageable pageable,
            final Long loginId,
            final String sort
    ) {
        Page<Article> articles;

        if (sort.equals("popularity")) {
            articles = articleRepository.findByLocationCityContainingAndStatusActive(keyword, Pageable.unpaged(),
                    ArticleStatus.ACTIVE);
            articles = sortByBookmarkCount(articles);

            articles = slicePage(pageable, articles);
        } else {
            String[] sortParams = sort.split(",");
            Sort.Direction direction = Sort.Direction.fromString(sortParams[1]);
            Sort sortOrder = Sort.by(direction, sortParams[0]);

            Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortOrder);
            articles = articleRepository.findByLocationCityContainingAndStatusActive(keyword, sortedPageable,
                    ArticleStatus.ACTIVE);
        }

        List<ArticleTotalResponse> articleResponses = articles.getContent().stream()
                .map(article -> {
                    boolean isEditable = article.getUser().getId().equals(loginId);
                    Long bookmarkCount = bookmarkRepository.countByArticleIdAndStatus(article.getId(),
                            BookmarkStatus.ACTIVE);
                    boolean isBookmarked = bookmarkRepository.existsByUserIdAndArticleIdAndStatus(
                            article.getUser().getId(), article.getId(), BookmarkStatus.ACTIVE);

                    return ArticleTotalResponse.of(
                            article,
                            bookmarkCount,
                            isBookmarked,
                            isEditable
                    );
                })
                .toList();

        return new PageImpl<>(articleResponses, pageable, articles.getTotalElements());
    }

    private Page<Article> slicePage(Pageable pageable, Page<Article> articles) {
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        int fromIndex = pageNumber * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, articles.getContent().size());

        List<Article> pagedArticles = articles.getContent().subList(fromIndex, toIndex);
        articles = new PageImpl<>(pagedArticles, pageable, articles.getTotalElements());
        return articles;
    }

    private Page<Article> sortByBookmarkCount(Page<Article> articles) {
        List<Article> sortedArticles = articles.stream()
                .sorted((a1, a2) -> {
                    Long count1 = bookmarkRepository.countByArticleIdAndStatus(a1.getId(), BookmarkStatus.ACTIVE);
                    Long count2 = bookmarkRepository.countByArticleIdAndStatus(a2.getId(), BookmarkStatus.ACTIVE);
                    return count2.compareTo(count1); // 북마크 수가 많은 순으로 내림차순 정렬
                })
                .collect(Collectors.toList());

        return new PageImpl<>(sortedArticles, articles.getPageable(), articles.getTotalElements());
    }

    // 아티클 삭제
    @Transactional
    public ArticleDeleteResponse deleteReview(final Long userId, final Long articleId) {
        final Article article = articleRepository.findByIdAndStatusIn(articleId,
                        List.of(ArticleStatus.ACTIVE, ArticleStatus.PRIVATE))
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.ARTICLE_NOT_FOUND, HttpStatus.NOT_FOUND));

        if (!article.getUser().getId().equals(userId)) {
            throw new BeApplicationException(ErrorCodes.ARTICLE_DELETE_NOT_USER, HttpStatus.FORBIDDEN);
        }

        article.delete();
        articleRepository.save(article);
        return ArticleDeleteResponse.from(true);
    }
}

