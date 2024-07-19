package site.travellaboratory.be.review.presentation.response.reader;

import java.util.List;

public record ReviewBannerListResponse(
    List<ReviewBannerResponse> reviews
) {
    public static ReviewBannerListResponse from(
        List<ReviewBannerResponse> reviews) {;
        return new ReviewBannerListResponse(reviews);
    }
}
