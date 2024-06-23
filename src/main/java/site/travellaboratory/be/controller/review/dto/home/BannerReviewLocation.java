package site.travellaboratory.be.controller.review.dto.home;

import site.travellaboratory.be.domain.article.Location;

public record BannerReviewLocation(
    String placeId,
    String address,
    String city
) {
    public static BannerReviewLocation from(Location location) {
        return new BannerReviewLocation(
            location.getPlaceId(),
            location.getAddress(),
            location.getCity()
        );
    }
}
