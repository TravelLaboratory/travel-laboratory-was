package site.travellaboratory.be.controller.review.dto.home;

import java.util.List;

public record ReviewBannerListResponse(
    List<ReviewBannerResponse> reviews
) {
    public static ReviewBannerListResponse from(
        List<ReviewBannerResponse> reviews) {;
        return new ReviewBannerListResponse(reviews);
    }
}
