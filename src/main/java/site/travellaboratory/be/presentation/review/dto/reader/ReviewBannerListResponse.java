package site.travellaboratory.be.presentation.review.dto.reader;

import java.util.List;

public record ReviewBannerListResponse(
    List<ReviewBannerResponse> reviews
) {
    public static ReviewBannerListResponse from(
        List<ReviewBannerResponse> reviews) {;
        return new ReviewBannerListResponse(reviews);
    }
}
