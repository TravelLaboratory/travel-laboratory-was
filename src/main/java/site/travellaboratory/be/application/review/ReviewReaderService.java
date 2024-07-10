package site.travellaboratory.be.application.review;

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
import site.travellaboratory.be.infrastructure.review.ReviewRepository;
import site.travellaboratory.be.infrastructure.review.entity.Review;
import site.travellaboratory.be.infrastructure.review.enums.ReviewStatus;
import site.travellaboratory.be.infrastructure.user.UserRepository;
import site.travellaboratory.be.infrastructure.user.entity.User;
import site.travellaboratory.be.infrastructure.user.enums.UserStatus;
import site.travellaboratory.be.infrastructure.userlikereview.UserLikeReviewRepository;
import site.travellaboratory.be.infrastructure.userlikereview.entity.UserLikeReview;
import site.travellaboratory.be.infrastructure.userlikereview.enums.UserLikeReviewStatus;
import site.travellaboratory.be.presentation.review.dto.ProfileReviewLocation;
import site.travellaboratory.be.presentation.review.dto.ProfileReviewPaginationResponse;
import site.travellaboratory.be.presentation.review.dto.ProfileReviewResponse;
import site.travellaboratory.be.presentation.review.dto.ReviewReadDetailResponse;
import site.travellaboratory.be.presentation.review.dto.home.BannerReviewLocation;
import site.travellaboratory.be.presentation.review.dto.home.ReviewBannerListResponse;
import site.travellaboratory.be.presentation.review.dto.home.ReviewBannerResponse;

@Service
@RequiredArgsConstructor
public class ReviewReaderService {

    private final ReviewRepository reviewRepository;
    private final UserLikeReviewRepository userLikeReviewRepository;
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
