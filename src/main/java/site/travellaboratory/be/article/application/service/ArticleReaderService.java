package site.travellaboratory.be.article.application.service;

import java.time.LocalDateTime;
import java.util.Collections;
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
import site.travellaboratory.be.article.domain.enums.ArticleStatus;
import site.travellaboratory.be.article.domain.enums.BookmarkStatus;
import site.travellaboratory.be.article.infrastructure.persistence.entity.ArticleEntity;
import site.travellaboratory.be.article.infrastructure.persistence.entity.BookmarkEntity;
import site.travellaboratory.be.article.infrastructure.persistence.repository.ArticleJpaRepository;
import site.travellaboratory.be.article.infrastructure.persistence.repository.BookmarkRepository;
import site.travellaboratory.be.article.presentation.response.like.BookmarkResponse;
import site.travellaboratory.be.article.presentation.response.reader.ArticleOneResponse;
import site.travellaboratory.be.article.presentation.response.reader.ArticleTotalResponse;
import site.travellaboratory.be.article.presentation.response.reader.BannerArticlesResponse;
import site.travellaboratory.be.common.error.ErrorCodes;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.user.domain.enums.UserStatus;
import site.travellaboratory.be.user.infrastructure.persistence.entity.UserEntity;
import site.travellaboratory.be.user.infrastructure.persistence.repository.UserJpaRepository;

@Service
@RequiredArgsConstructor
public class ArticleReaderService {

    private final ArticleJpaRepository articleJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final BookmarkRepository bookmarkRepository;

    // 내 초기 여행 계획 전체 조회
    @Transactional
    public Page<ArticleTotalResponse> findByUserArticles(final Long loginId, final Long userId, Pageable pageable) {
        // 사용자 조회
        UserEntity userEntity = userJpaRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        // 로그인한 사용자가 이 아티클을 작성하였는지 (수정 가능한 지) 확인
        boolean isEditable = userEntity.getId().equals(loginId);

        // 사용자가 작성한 아티클 또는 다른 사용자의 공개된 아티클 목록 조회
        Page<ArticleEntity> articlesPage;
        if (isEditable) {
            articlesPage = articleJpaRepository.findByUserEntityAndStatus(userEntity, ArticleStatus.ACTIVE, pageable)
                    .orElseThrow(() -> new BeApplicationException(ErrorCodes.ARTICLE_NOT_FOUND, HttpStatus.NOT_FOUND));
        } else {
            articlesPage = articleJpaRepository.findByUserEntityAndStatus(userEntity, ArticleStatus.ACTIVE, pageable)
                    .orElseThrow(() -> new BeApplicationException(ErrorCodes.ARTICLE_NOT_FOUND, HttpStatus.NOT_FOUND));
        }

        // 각 아티클의 북마크 수 및 북마크 상태 확인하여 ArticleResponse로 변환
        List<ArticleTotalResponse> articleResponses = articlesPage.getContent().stream()
                .map(article -> {
                    Long bookmarkCount = bookmarkRepository.countByArticleIdAndStatus(article.getId(),
                            BookmarkStatus.ACTIVE);
                    boolean isBookmarked = bookmarkRepository.existsByUserEntityIdAndArticleEntityIdAndStatus(loginId,
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
        final ArticleEntity articleEntity = articleJpaRepository.findByIdAndStatus(articleId, ArticleStatus.ACTIVE)
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.ARTICLE_NOT_FOUND, HttpStatus.NOT_FOUND));

        final Long bookmarkCount = bookmarkRepository.countByArticleIdAndStatus(articleId, BookmarkStatus.ACTIVE);

        boolean isBookmarked = bookmarkRepository.existsByUserEntityIdAndArticleEntityIdAndStatus(loginId, articleId,
                BookmarkStatus.ACTIVE);


        boolean isEditable = articleEntity.getUserEntity().getId().equals(loginId);

        return ArticleOneResponse.of(articleEntity, bookmarkCount, isBookmarked, isEditable);
    }

    // 아티클 검색
    @Transactional
    public Page<ArticleTotalResponse> searchArticlesByKeyWord(
            final String keyword,
            final Pageable pageable,
            final Long loginId,
            final String sort
    ) {
        List<ArticleEntity> articleJpaEntities;
        Page<ArticleEntity> newArticles;

        if (sort.equals("popularity")) {
            articleJpaEntities = articleJpaRepository.findByLocationCityContainingAndStatusActive(keyword, ArticleStatus.ACTIVE);
            articleJpaEntities = sortByBookmarkCount(articleJpaEntities);
            newArticles = slicePage(pageable, articleJpaEntities);
        } else {
            String[] sortParams = sort.split(",");
            Sort.Direction direction = Sort.Direction.fromString(sortParams[1]);
            Sort sortOrder = Sort.by(direction, sortParams[0]);

            Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortOrder);
            newArticles = articleJpaRepository.findByLocationCityContainingAndStatusActive(keyword, sortedPageable,
                    ArticleStatus.ACTIVE);
        }

        List<ArticleTotalResponse> articleResponses = newArticles.getContent().stream()
                .map(article -> {
                    boolean isEditable = article.getUserEntity().getId().equals(loginId);
                    Long bookmarkCount = bookmarkRepository.countByArticleIdAndStatus(article.getId(),
                            BookmarkStatus.ACTIVE);
                    boolean isBookmarked = bookmarkRepository.existsByUserEntityIdAndArticleEntityIdAndStatus(
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
    public List<BannerArticlesResponse> readBannerArticlesHot() {
        // 한 달 전 시간 계산
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusWeeks(1);

        // 좋아요 수 기준으로 상위 12개의 articleId 가져오기
        Pageable pageable = PageRequest.of(0, 12);
        List<Long> topArticleIds = bookmarkRepository.findTopArticleIdsByLikeCount(oneMonthAgo, pageable);

        // 해당 articleId 리스트로 게시글 조회
        List<ArticleEntity> articles = articleJpaRepository.findActiveArticlesWithUserByIds(topArticleIds);
        articleJpaRepository.findActiveArticlesWithLocationsByIds(topArticleIds);
        articleJpaRepository.findActiveArticlesWithTravelStylesByIds(topArticleIds);


        return articles.stream()
            .map(BannerArticlesResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public Page<BookmarkResponse> findAllBookmarkByUser(final Long loginId, final Long userId, Pageable pageable) {
        final UserEntity loginUserEntity = userJpaRepository.findByIdAndStatus(loginId, UserStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND,
                HttpStatus.NOT_FOUND));

        final UserEntity userEntity = userJpaRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND,
                HttpStatus.NOT_FOUND));

        final Page<BookmarkEntity> bookmarks = bookmarkRepository.findByUserEntityAndStatus(
                userEntity, BookmarkStatus.ACTIVE, pageable)
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.BOOKMARK_NOT_FOUND, HttpStatus.NOT_FOUND));

        List<BookmarkResponse> bookmarkResponses = bookmarks.stream()
            .map(bookmark -> {
                final Long bookmarkCount = bookmarkRepository.countByArticleIdAndStatus(
                    bookmark.getArticleEntity().getId(),
                    BookmarkStatus.ACTIVE);
                boolean isBookmarked = bookmarkRepository.existsByUserEntityIdAndArticleEntityIdAndStatus(
                    loginUserEntity.getId(), bookmark.getArticleEntity().getId(),
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

    private Page<ArticleEntity> slicePage(Pageable pageable, List<ArticleEntity> articleJpaEntities) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<ArticleEntity> list;

        if (articleJpaEntities.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, articleJpaEntities.size());
            list = articleJpaEntities.subList(startItem, toIndex);
        }

        return new PageImpl<>(list, pageable, articleJpaEntities.size());
    }

    private List<ArticleEntity> sortByBookmarkCount(List<ArticleEntity> articleJpaEntities) {
        return articleJpaEntities.stream()
                .sorted((a1, a2) -> {
                    Long count1 = bookmarkRepository.countByArticleIdAndStatus(a1.getId(), BookmarkStatus.ACTIVE);
                    Long count2 = bookmarkRepository.countByArticleIdAndStatus(a2.getId(), BookmarkStatus.ACTIVE);
                    return count2.compareTo(count1); // 북마크 수가 많은 순으로 내림차순 정렬
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public Page<ArticleTotalResponse> searchAllArticles(final Long loginId, Pageable pageable, String sort) {
        userJpaRepository.findByIdAndStatus(loginId, UserStatus.ACTIVE)
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        if (sort.equals("popularity")) {
            List<ArticleEntity> articleJpaEntities = articleJpaRepository.findAllByStatus(ArticleStatus.ACTIVE);
            articleJpaEntities = articleJpaEntities.stream()
                    .sorted((a1, a2) -> {
                        Long count1 = bookmarkRepository.countByArticleIdAndStatus(a1.getId(), BookmarkStatus.ACTIVE);
                        Long count2 = bookmarkRepository.countByArticleIdAndStatus(a2.getId(), BookmarkStatus.ACTIVE);
                        return count2.compareTo(count1); // 북마크 수가 많은 순으로 내림차순 정렬
                    })
                    .collect(Collectors.toList());

            Page<ArticleEntity> pagedArticles = slicePage(pageable, articleJpaEntities);
            return getArticleTotalResponses(loginId, pageable, pagedArticles);
        } else {
            final Page<ArticleEntity> articles = articleJpaRepository.findAllByStatusOrderByCreatedAtDesc(ArticleStatus.ACTIVE, pageable);
            return getArticleTotalResponses(loginId, pageable, articles);
        }
    }

    private Page<ArticleTotalResponse> getArticleTotalResponses(Long loginId, Pageable pageable, Page<ArticleEntity> articles) {
        List<ArticleTotalResponse> articleResponses = articles.getContent().stream()
                .map(article -> {
                    boolean isEditable = article.getUserEntity().getId().equals(loginId);
                    Long bookmarkCount = bookmarkRepository.countByArticleIdAndStatus(article.getId(), BookmarkStatus.ACTIVE);
                    boolean isBookmarked = bookmarkRepository.existsByUserEntityIdAndArticleEntityIdAndStatus(loginId, article.getId(), BookmarkStatus.ACTIVE);

                    return ArticleTotalResponse.of(article, bookmarkCount, isBookmarked, isEditable);
                })
                .toList();

        return new PageImpl<>(articleResponses, pageable, articles.getTotalElements());
    }
}

