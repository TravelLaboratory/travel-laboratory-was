package site.travellaboratory.be.presentation.review.dto.home;

import site.travellaboratory.be.infrastructure.domains.article.entity.Location;

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
