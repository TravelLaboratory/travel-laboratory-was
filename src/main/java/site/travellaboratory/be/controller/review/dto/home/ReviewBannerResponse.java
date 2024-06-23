package site.travellaboratory.be.controller.review.dto.home;

import java.util.List;
import site.travellaboratory.be.domain.review.Review;

public record ReviewBannerResponse(
    Long reviewId,
    String title,
    String representativeImgUrl,
    List<BannerReviewLocation> location,
    String nickname,
    String profileImgUrl
) {
    public static ReviewBannerResponse from(Review review, List<BannerReviewLocation> locations) {
        return new ReviewBannerResponse(
            review.getId(),
            review.getTitle(),
            review.getRepresentativeImgUrl(),
            locations,
            review.getUser().getNickname(),
            review.getUser().getProfileImgUrl()
        );
    }
}
