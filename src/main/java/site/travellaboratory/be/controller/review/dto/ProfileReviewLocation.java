package site.travellaboratory.be.controller.review.dto;

import site.travellaboratory.be.domain.article.Location;

public record ProfileReviewLocation(
    String placeId,
    String address,
    String city
) {
    public static ProfileReviewLocation from(Location location) {
        return new ProfileReviewLocation(
            location.getPlaceId(),
            location.getAddress(),
            location.getCity()
        );
    }
}
