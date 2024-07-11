package site.travellaboratory.be.application.article;

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
import site.travellaboratory.be.infrastructure.domains.article.ArticleRepository;
import site.travellaboratory.be.infrastructure.domains.article.entity.Article;
import site.travellaboratory.be.infrastructure.domains.article.enums.ArticleStatus;
import site.travellaboratory.be.infrastructure.domains.bookmark.BookmarkRepository;
import site.travellaboratory.be.infrastructure.domains.bookmark.entity.Bookmark;
import site.travellaboratory.be.infrastructure.domains.bookmark.enums.BookmarkStatus;
import site.travellaboratory.be.infrastructure.domains.user.UserRepository;
import site.travellaboratory.be.infrastructure.domains.user.entity.User;
import site.travellaboratory.be.infrastructure.domains.user.enums.UserStatus;
import site.travellaboratory.be.presentation.article.dto.reader.ArticleOneResponse;
import site.travellaboratory.be.presentation.article.dto.reader.ArticleTotalResponse;
import site.travellaboratory.be.presentation.article.dto.like.BookmarkResponse;

@Service
@RequiredArgsConstructor
public class ArticleReaderService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final BookmarkRepository bookmarkRepository;

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
    public ArticleOneResponse findByArticle(final Long loginId, final Long articleId) {
        // 유효하지 않은 아티클 조회 할 경우
        final Article article = articleRepository.findByIdAndStatusIn(articleId,
                        List.of(ArticleStatus.ACTIVE, ArticleStatus.PRIVATE))
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.ARTICLE_NOT_FOUND, HttpStatus.NOT_FOUND));

        final Long bookmarkCount = bookmarkRepository.countByArticleIdAndStatus(articleId, BookmarkStatus.ACTIVE);

        boolean isBookmarked = bookmarkRepository.existsByUserIdAndArticleIdAndStatus(loginId, articleId,
                BookmarkStatus.ACTIVE);

        boolean isPrivate = (article.getStatus() == ArticleStatus.PRIVATE);

        boolean isEditable = article.getUser().getId().equals(loginId);

        return ArticleOneResponse.of(article, bookmarkCount, isBookmarked, isPrivate, isEditable);
    }

    // 아티클 검색
    @Transactional
    public Page<ArticleTotalResponse> searchArticlesByKeyWord(
            final String keyword,
            final Pageable pageable,
            final Long loginId,
            final String sort
    ) {
        List<Article> articles;
        Page<Article> newArticles;

        if (sort.equals("popularity")) {
            articles = articleRepository.findByLocationCityContainingAndStatusActive(keyword, ArticleStatus.ACTIVE);
            articles = sortByBookmarkCount(articles);
            newArticles = slicePage(pageable, articles);
        } else {
            String[] sortParams = sort.split(",");
            Sort.Direction direction = Sort.Direction.fromString(sortParams[1]);
            Sort sortOrder = Sort.by(direction, sortParams[0]);

            Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortOrder);
            newArticles = articleRepository.findByLocationCityContainingAndStatusActive(keyword, sortedPageable,
                    ArticleStatus.ACTIVE);
        }

        List<ArticleTotalResponse> articleResponses = newArticles.getContent().stream()
                .map(article -> {
                    boolean isEditable = article.getUser().getId().equals(loginId);
                    Long bookmarkCount = bookmarkRepository.countByArticleIdAndStatus(article.getId(),
                            BookmarkStatus.ACTIVE);
                    boolean isBookmarked = bookmarkRepository.existsByUserIdAndArticleIdAndStatus(
                            loginId, article.getId(), BookmarkStatus.ACTIVE);

                    return ArticleTotalResponse.of(
                            article,
                            bookmarkCount,
                            isBookmarked,
                            isEditable
                    );
                })
                .collect(Collectors.toList());

        return new PageImpl<>(articleResponses, pageable, newArticles.getTotalElements());
    }

    @Transactional
    public List<ArticleTotalResponse> getBannerNotUserArticles() {
        List<Article> articles = articleRepository.findAllByStatus(ArticleStatus.ACTIVE);

        return articles.stream()
                .map(article -> {
                    Long bookmarkCount = bookmarkRepository.countByArticleIdAndStatus(article.getId(),
                            BookmarkStatus.ACTIVE);
                    boolean isBookmarked = false;
                    boolean isEditable = false;

                    return ArticleTotalResponse.of(article, bookmarkCount, isBookmarked, isEditable);
                })
                .sorted((a1, a2) -> a2.bookmarkCount().compareTo(a1.bookmarkCount())) // 북마크 수 기준으로 내림차순 정렬
                .limit(4) // 상위 4개 아티클만 가져옴
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ArticleTotalResponse> getBannerUserArticles(final Long userId) {
        userRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        List<Article> articles = articleRepository.findAllByStatus(ArticleStatus.ACTIVE);

        return articles.stream()
                .map(article -> {
                    Long bookmarkCount = bookmarkRepository.countByArticleIdAndStatus(article.getId(),
                            BookmarkStatus.ACTIVE);
                    boolean isBookmarked = bookmarkRepository.existsByUserIdAndArticleIdAndStatus(userId,
                            article.getId(),
                            BookmarkStatus.ACTIVE);
                    boolean isEditable = userId.equals(article.getUser().getId());

                    return ArticleTotalResponse.of(article, bookmarkCount, isBookmarked, isEditable);
                })
                .sorted((a1, a2) -> a2.bookmarkCount().compareTo(a1.bookmarkCount())) // 북마크 수 기준으로 내림차순 정렬
                .limit(4) // 상위 4개 아티클만 가져옴
                .collect(Collectors.toList());
    }


    @Transactional
    public Page<BookmarkResponse> findAllBookmarkByUser(final Long loginId, final Long userId, Pageable pageable) {
        final User loginUser = userRepository.findByIdAndStatus(loginId, UserStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND,
                HttpStatus.NOT_FOUND));

        final User user = userRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND,
                HttpStatus.NOT_FOUND));

        final Page<Bookmark> bookmarks = bookmarkRepository.findByUserAndStatusIn(user,
                List.of(BookmarkStatus.ACTIVE), pageable)
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.BOOKMARK_NOT_FOUND, HttpStatus.NOT_FOUND));

        List<BookmarkResponse> bookmarkResponses = bookmarks.stream()
            .map(bookmark -> {
                final Long bookmarkCount = bookmarkRepository.countByArticleIdAndStatus(bookmark.getArticle().getId(),
                    BookmarkStatus.ACTIVE);
                boolean isBookmarked = bookmarkRepository.existsByUserIdAndArticleIdAndStatus(
                    loginUser.getId(), bookmark.getArticle().getId(),
                    BookmarkStatus.ACTIVE);

                return BookmarkResponse.of(
                    bookmark,
                    bookmarkCount,
                    isBookmarked
                );
            })
            .toList();
        return new PageImpl<>(bookmarkResponses, pageable, bookmarks.getTotalElements());
    }

    private Page<Article> slicePage(Pageable pageable, List<Article> articles) {
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        int fromIndex = pageNumber * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, articles.size());

        List<Article> pagedArticles = articles.subList(fromIndex, toIndex);
        return new PageImpl<>(pagedArticles, pageable, articles.size());
    }

    private List<Article> sortByBookmarkCount(List<Article> articles) {
        return articles.stream()
            .sorted((a1, a2) -> {
                Long count1 = bookmarkRepository.countByArticleIdAndStatus(a1.getId(), BookmarkStatus.ACTIVE);
                Long count2 = bookmarkRepository.countByArticleIdAndStatus(a2.getId(), BookmarkStatus.ACTIVE);
                return count2.compareTo(count1); // 북마크 수가 많은 순으로 내림차순 정렬
            })
            .collect(Collectors.toList());
    }
}

