package site.travellaboratory.be.presentation.review.dto.reader;

import java.util.List;
import site.travellaboratory.be.infrastructure.domains.review.entity.ReviewJpaEntity;

public record ReviewBannerResponse(
    Long reviewId,
    String title,
    String representativeImgUrl,
    List<BannerReviewLocation> location,
    String nickname,
    String profileImgUrl
) {
    public static ReviewBannerResponse from(ReviewJpaEntity reviewJpaEntity, List<BannerReviewLocation> locations) {
        return new ReviewBannerResponse(
            reviewJpaEntity.getId(),
            reviewJpaEntity.getTitle(),
            reviewJpaEntity.getRepresentativeImgUrl(),
            locations,
            reviewJpaEntity.getUserJpaEntity().getNickname(),
            reviewJpaEntity.getUserJpaEntity().getProfileImgUrl()
        );
    }
}
