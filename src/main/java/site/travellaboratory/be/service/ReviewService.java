package site.travellaboratory.be.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.controller.review.dto.ProfileReviewLocation;
import site.travellaboratory.be.controller.review.dto.ProfileReviewPaginationResponse;
import site.travellaboratory.be.controller.review.dto.ProfileReviewResponse;
import site.travellaboratory.be.controller.review.dto.ReviewDeleteResponse;
import site.travellaboratory.be.controller.review.dto.ReviewReadDetailResponse;
import site.travellaboratory.be.controller.review.dto.ReviewSaveRequest;
import site.travellaboratory.be.controller.review.dto.ReviewSaveResponse;
import site.travellaboratory.be.controller.review.dto.ReviewUpdateRequest;
import site.travellaboratory.be.controller.review.dto.ReviewUpdateResponse;
import site.travellaboratory.be.controller.review.dto.home.BannerReviewLocation;
import site.travellaboratory.be.controller.review.dto.home.ReviewBannerListResponse;
import site.travellaboratory.be.controller.review.dto.home.ReviewBannerResponse;
import site.travellaboratory.be.controller.review.dto.userlikereview.ReviewToggleLikeResponse;
import site.travellaboratory.be.domain.article.Article;
import site.travellaboratory.be.domain.article.ArticleRepository;
import site.travellaboratory.be.domain.article.ArticleStatus;
import site.travellaboratory.be.domain.review.Review;
import site.travellaboratory.be.domain.review.ReviewRepository;
import site.travellaboratory.be.domain.review.ReviewStatus;
import site.travellaboratory.be.domain.user.UserRepository;
import site.travellaboratory.be.domain.user.entity.User;
import site.travellaboratory.be.domain.user.entity.UserStatus;
import site.travellaboratory.be.domain.userlikereview.UserLikeReview;
import site.travellaboratory.be.domain.userlikereview.UserLikeReviewRepository;
import site.travellaboratory.be.domain.userlikereview.UserLikeReviewStatus;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ArticleRepository articleRepository;
    private final UserLikeReviewRepository userLikeReviewRepository;
    // todo: refactoring homeService, homeController 만들기.
    private final UserRepository userRepository;

    public ReviewReadDetailResponse readReviewDetail(Long userId, Long reviewId) {
        // 유효하지 않은 후기를 조회할 경우
        Review review = reviewRepository.findByIdAndStatusIn(reviewId,
                List.of(ReviewStatus.ACTIVE, ReviewStatus.PRIVATE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.REVIEW_READ_DETAIL_INVALID,
                HttpStatus.NOT_FOUND));

        // 나만보기 상태의 후기를 다른 유저가 조회할 경우
        if (review.getStatus() == ReviewStatus.PRIVATE && (!review.getUser().getId()
            .equals(userId))) {
            throw new BeApplicationException(ErrorCodes.REVIEW_READ_DETAIL_NOT_AUTHORIZATION,
                HttpStatus.FORBIDDEN);
        }

        // 후기 조회
        // (1) 수정, 삭제 권한
        boolean isEditable = review.getUser().getId().equals(userId);

        // (2) 좋아요
        boolean isLike = userLikeReviewRepository.findByUserIdAndReviewId(userId, reviewId)
            .map(UserLikeReview::getStatus)
            .orElse(UserLikeReviewStatus.INACTIVE) == UserLikeReviewStatus.ACTIVE;

        // (3) 좋아요 개수
        long likeCount = userLikeReviewRepository.countByReviewIdAndStatus(reviewId,
            UserLikeReviewStatus.ACTIVE);

        return ReviewReadDetailResponse.from(
            review, isEditable, isLike, likeCount
        );
    }

    @Transactional
    public ReviewSaveResponse saveReview(Long userId, ReviewSaveRequest request) {
        // 유효하지 않은 여행 계획에 대한 후기를 작성할 경우
        Article article = articleRepository.findByIdAndStatusIn(request.articleId(),
                List.of(ArticleStatus.ACTIVE, ArticleStatus.PRIVATE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.REVIEW_POST_INVALID,
                HttpStatus.NOT_FOUND));

        // 유저가 작성한 article_id이 아닌 경우
        if (!article.getUser().getId().equals(userId)) {
            throw new BeApplicationException(ErrorCodes.REVIEW_POST_NOT_USER, HttpStatus.FORBIDDEN);
        }

        // 이미 해당 여행 계획에 대한 후기가 있을 경우
        reviewRepository.findByArticleAndStatusInOrderByArticleDesc(article,
                List.of(ReviewStatus.ACTIVE, ReviewStatus.PRIVATE))
            .ifPresent(it -> {
                throw new BeApplicationException(ErrorCodes.REVIEW_POST_EXIST,
                    HttpStatus.CONFLICT);
            });

        // 본인이 작성한 여행 계획 + 후기가 없는 경우
        Review saveReview = reviewRepository.save(
            Review.of(
                article.getUser(),
                article,
                request.title(),
                request.representativeImgUrl(),
                request.description(),
                request.status()
            )
        );
        return ReviewSaveResponse.from(saveReview.getId());
    }

    @Transactional
    public ReviewUpdateResponse updateReview(Long userId, Long reviewId,
        ReviewUpdateRequest request) {
        // 유효하지 않은 후기를 수정할 경우
        Review review = reviewRepository.findByIdAndStatusIn(reviewId,
                List.of(ReviewStatus.ACTIVE, ReviewStatus.PRIVATE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.REVIEW_UPDATE_INVALID,
                HttpStatus.NOT_FOUND));

        // 유저가 작성한 후기가 아닌 경우
        if (!review.getUser().getId().equals(userId)) {
            throw new BeApplicationException(ErrorCodes.REVIEW_UPDATE_NOT_USER,
                HttpStatus.FORBIDDEN);
        }

        // 후기 업데이트
        review.update(request.title(), request.representativeImgUrl(), request.description(),
            request.status());
        Review updateReview = reviewRepository.save(review);
        return ReviewUpdateResponse.from(updateReview.getId());
    }

    @Transactional
    public ReviewDeleteResponse deleteReview(final Long userId, final Long reviewId) {
        // 유효하지 않은 후기를 삭제할 경우
        Review review = reviewRepository.findByIdAndStatusIn(reviewId,
                List.of(ReviewStatus.ACTIVE, ReviewStatus.PRIVATE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.REVIEW_DELETE_INVALID,
                HttpStatus.NOT_FOUND));

        // 유저가 작성한 후기가 아닌 경우
        if (!review.getUser().getId().equals(userId)) {
            throw new BeApplicationException(ErrorCodes.REVIEW_DELETE_NOT_USER,
                HttpStatus.FORBIDDEN);
        }

        // 후기 삭제
        review.delete();
        reviewRepository.save(review);
        return ReviewDeleteResponse.from(true);
    }

    @Transactional
    public ReviewToggleLikeResponse toggleLikeReview(Long userId, Long reviewId) {
        // 유효하지 않은 후기를 좋아요 할 경우
        Review review = reviewRepository.findByIdAndStatusIn(reviewId,
                List.of(ReviewStatus.ACTIVE, ReviewStatus.PRIVATE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.REVIEW_LIKE_INVALID,
                HttpStatus.NOT_FOUND));

        UserLikeReview userLikeReview = userLikeReviewRepository.findByUserIdAndReviewId(userId,
                reviewId)
            .orElse(null);

        // 처음 좋아요를 누른 게 아닌 경우
        if (userLikeReview != null) {
            userLikeReview.toggleStatus();
        } else {
            // 처음 좋아요를 누른 경우 - 새로 생성
            User user = User.of(userId);
            userLikeReview = UserLikeReview.of(user, review);
        }

        UserLikeReview saveLikeReview = userLikeReviewRepository.save(userLikeReview);
        return ReviewToggleLikeResponse.from(saveLikeReview.getStatus());
    }


    /*
     * 프로필 - 후기 전체 조회  [페이지네이션]
     * */
    public ProfileReviewPaginationResponse readProfileReviews(
        Long tokenUserId, Long userId, int page, int size) {
        User user = userRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.PROFILE_REVIEW_READ_USER_NOT_FOUND,
                    HttpStatus.NOT_FOUND));

        PageRequest pageable = PageRequest.of(page, size);

        Page<Long> reviewIdsPage;
        // (1) 페이징 적용을 위한 Review Id 목록 가져와서
        if (tokenUserId.equals(userId)) {
            reviewIdsPage = reviewRepository.findReviewIdsByUserAndStatusInOrderByCreatedAt(user,
                List.of(ReviewStatus.ACTIVE, ReviewStatus.PRIVATE), pageable);
        } else { // (2) 다를 경우에는 PRIVATE 제외
            reviewIdsPage = reviewRepository.findReviewIdsByUserAndStatusInOrderByCreatedAt(user,
                List.of(ReviewStatus.ACTIVE), pageable);
        }

        // (2) id 목록으로 연관 엔티티 포함한 리뷰 목록 가져오기
        List<Review> reviews = reviewRepository.findReviewsWithArticlesAndLocationsByIds(
            reviewIdsPage.getContent());

        List<ProfileReviewResponse> reviewPage = reviews.stream()
            .map(this::toProfileReviewResponse)
            .toList();

        return ProfileReviewPaginationResponse.from(reviewPage, reviewIdsPage);
    }

    /*
     * 홈(배너) 최신 여행 후기 - 조회 리스트 8개 [feat. 비회원, 회원 공통 항상]
     * */
    @Transactional(readOnly = true)
    public ReviewBannerListResponse readBannerReviews() {
        PageRequest pageable = PageRequest.of(0, 8);

        //(1) limit이 없기에 페이징을 적용해서 리뷰 id 목록 가져오기
        Page<Long> reviewIdsPage = reviewRepository.findReviewIdsByStatusOrderByCreatedAt(pageable);

        List<Review> reviews = reviewRepository.findReviewsWithArticlesAndLocationsByIdsAndUserStatus(reviewIdsPage.getContent());

        List<ReviewBannerResponse> list = reviews.stream()
            .map(this::toReviewBannerResponse)
            .toList();

        return ReviewBannerListResponse.from(list);
    }

    private ProfileReviewResponse toProfileReviewResponse(Review review) {
        List<ProfileReviewLocation> locations = review.getArticle().getLocation().stream()
            .map(ProfileReviewLocation::from)
            .collect(Collectors.toList());
        return ProfileReviewResponse.from(review, locations);
    }

    private ReviewBannerResponse toReviewBannerResponse(Review review) {
        List<BannerReviewLocation> locations = review.getArticle().getLocation().stream()
            .map(BannerReviewLocation::from)
            .collect(Collectors.toList());
        return ReviewBannerResponse.from(review, locations);
    }
}
