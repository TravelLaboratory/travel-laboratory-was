package site.travellaboratory.be.presentation.review.dto.reader;

import java.util.List;
import site.travellaboratory.be.infrastructure.domains.review.entity.ReviewEntity;

public record ReviewBannerResponse(
    Long reviewId,
    String title,
    String representativeImgUrl,
    List<BannerReviewLocation> location,
    String nickname,
    String profileImgUrl
) {
    public static ReviewBannerResponse from(ReviewEntity reviewEntity, List<BannerReviewLocation> locations) {
        return new ReviewBannerResponse(
            reviewEntity.getId(),
            reviewEntity.getTitle(),
            reviewEntity.getRepresentativeImgUrl(),
            locations,
            reviewEntity.getUserEntity().getNickname(),
            reviewEntity.getUserEntity().getProfileImgUrl()
        );
    }
}
