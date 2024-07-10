package site.travellaboratory.be.presentation.review.dto;

import site.travellaboratory.be.infrastructure.article.entity.Location;

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
