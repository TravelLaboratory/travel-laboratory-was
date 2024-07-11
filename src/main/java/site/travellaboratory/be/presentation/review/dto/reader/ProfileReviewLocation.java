package site.travellaboratory.be.presentation.review.dto.reader;

import site.travellaboratory.be.infrastructure.domains.article.entity.Location;

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
