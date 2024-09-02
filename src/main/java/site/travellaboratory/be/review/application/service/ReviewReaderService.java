package site.travellaboratory.be.review.application.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import site.travellaboratory.be.common.presentation.error.ErrorCodes;
import site.travellaboratory.be.common.presentation.exception.BeApplicationException;
import site.travellaboratory.be.review.domain.enums.ReviewLikeStatus;
import site.travellaboratory.be.review.domain.enums.ReviewStatus;
import site.travellaboratory.be.review.infrastructure.persistence.entity.ReviewEntity;
import site.travellaboratory.be.review.infrastructure.persistence.entity.ReviewLikeEntity;
import site.travellaboratory.be.review.infrastructure.persistence.repository.ReviewJpaRepository;
import site.travellaboratory.be.review.infrastructure.persistence.repository.ReviewLikeJpaRepository;
import site.travellaboratory.be.review.presentation.response.reader.ProfileReviewLocation;
import site.travellaboratory.be.review.presentation.response.reader.ProfileReviewPaginationResponse;
import site.travellaboratory.be.review.presentation.response.reader.ProfileReviewResponse;
import site.travellaboratory.be.review.presentation.response.reader.ReviewReadDetailResponse;
import site.travellaboratory.be.user.domain.enums.UserStatus;
import site.travellaboratory.be.user.infrastructure.persistence.entity.UserEntity;
import site.travellaboratory.be.user.infrastructure.persistence.repository.UserJpaRepository;

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

    private ProfileReviewResponse toProfileReviewResponse(ReviewEntity reviewEntity) {
        List<ProfileReviewLocation> locations = reviewEntity.getArticleEntity().getLocationEntities().stream()
            .map(ProfileReviewLocation::from)
            .collect(Collectors.toList());
        return ProfileReviewResponse.from(reviewEntity, locations);
    }
}
