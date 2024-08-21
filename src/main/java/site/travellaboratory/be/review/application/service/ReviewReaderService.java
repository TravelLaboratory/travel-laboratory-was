package site.travellaboratory.be.review.application.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.error.ErrorCodes;
import site.travellaboratory.be.review.infrastructure.persistence.repository.ReviewJpaRepository;
import site.travellaboratory.be.review.infrastructure.persistence.entity.ReviewEntity;
import site.travellaboratory.be.review.domain.enums.ReviewStatus;
import site.travellaboratory.be.user.infrastructure.persistence.repository.UserJpaRepository;
import site.travellaboratory.be.user.infrastructure.persistence.entity.UserEntity;
import site.travellaboratory.be.user.domain.enums.UserStatus;
import site.travellaboratory.be.review.infrastructure.persistence.repository.ReviewLikeJpaRepository;
import site.travellaboratory.be.review.infrastructure.persistence.entity.ReviewLikeEntity;
import site.travellaboratory.be.review.domain.enums.ReviewLikeStatus;
import site.travellaboratory.be.review.presentation.response.reader.ProfileReviewLocation;
import site.travellaboratory.be.review.presentation.response.reader.ProfileReviewPaginationResponse;
import site.travellaboratory.be.review.presentation.response.reader.ProfileReviewResponse;
import site.travellaboratory.be.review.presentation.response.reader.ReviewReadDetailResponse;
import site.travellaboratory.be.review.presentation.response.reader.BannerReviewLocation;
import site.travellaboratory.be.review.presentation.response.reader.ReviewBannerListResponse;
import site.travellaboratory.be.review.presentation.response.reader.ReviewBannerResponse;

@Service
@RequiredArgsConstructor
public class ReviewReaderService {

    private final ReviewJpaRepository reviewJpaRepository;
    private final ReviewLikeJpaRepository reviewLikeJpaRepository;
    private final UserJpaRepository userJpaRepository;

    public ReviewReadDetailResponse readReviewDetail(Long userId, Long reviewId) {
        // 유효하지 않은 후기를 조회할 경우
        ReviewEntity reviewEntity = reviewJpaRepository.findByIdAndStatus(reviewId, ReviewStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.REVIEW_READ_DETAIL_INVALID,
                HttpStatus.NOT_FOUND));

        // 후기 조회
        // (1) 수정, 삭제 권한
        boolean isEditable = reviewEntity.getUserEntity().getId().equals(userId);

        // (2) 좋아요
        boolean isLike = reviewLikeJpaRepository.findByUserIdAndReviewId(userId, reviewId)
            .map(ReviewLikeEntity::getStatus)
            .orElse(ReviewLikeStatus.INACTIVE) == ReviewLikeStatus.ACTIVE;

        // (3) 좋아요 개수
        long likeCount = reviewLikeJpaRepository.countByReviewEntityIdAndStatus(reviewId,
            ReviewLikeStatus.ACTIVE);

        return ReviewReadDetailResponse.from(
            reviewEntity, isEditable, isLike, likeCount
        );
    }

    /*
     * 프로필 - 후기 전체 조회  [페이지네이션]
     * */
    public ProfileReviewPaginationResponse readProfileReviews(
        Long tokenUserId, Long userId, int page, int size) {
        UserEntity userEntity = userJpaRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.PROFILE_REVIEW_READ_USER_NOT_FOUND,
                    HttpStatus.NOT_FOUND));

        PageRequest pageable = PageRequest.of(page, size);

        Page<Long> reviewIdsPage;
        // (1) 페이징 적용을 위한 Review Id 목록 가져와서
        if (tokenUserId.equals(userId)) {
            reviewIdsPage = reviewJpaRepository.findReviewIdsByUserAndStatusOrderByCreatedAt(
                userEntity,
                ReviewStatus.ACTIVE, pageable);
        } else { // (2) 다를 경우에는 PRIVATE 제외
            reviewIdsPage = reviewJpaRepository.findReviewIdsByUserAndStatusOrderByCreatedAt(
                userEntity,
                ReviewStatus.ACTIVE, pageable);
        }

        // (2) id 목록으로 연관 엔티티 포함한 리뷰 목록 가져오기
        List<ReviewEntity> reviewJpaEntities = reviewJpaRepository.findReviewsWithArticlesAndLocationsByIds(
            reviewIdsPage.getContent());

        List<ProfileReviewResponse> reviewPage = reviewJpaEntities.stream()
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
        Page<Long> reviewIdsPage = reviewJpaRepository.findReviewIdsByStatusOrderByCreatedAt(pageable);

        List<ReviewEntity> reviewJpaEntities = reviewJpaRepository.findReviewsWithArticlesAndLocationsByIdsAndUserStatus(reviewIdsPage.getContent());

        List<ReviewBannerResponse> list = reviewJpaEntities.stream()
            .map(this::toReviewBannerResponse)
            .toList();

        return ReviewBannerListResponse.from(list);
    }

    private ProfileReviewResponse toProfileReviewResponse(ReviewEntity reviewEntity) {
        List<ProfileReviewLocation> locations = reviewEntity.getArticleEntity().getLocationEntities().stream()
            .map(ProfileReviewLocation::from)
            .collect(Collectors.toList());
        return ProfileReviewResponse.from(reviewEntity, locations);
    }

    private ReviewBannerResponse toReviewBannerResponse(ReviewEntity reviewEntity) {
        List<BannerReviewLocation> locations = reviewEntity.getArticleEntity().getLocationEntities().stream()
            .map(BannerReviewLocation::from)
            .collect(Collectors.toList());
        return ReviewBannerResponse.from(reviewEntity, locations);
    }
}
