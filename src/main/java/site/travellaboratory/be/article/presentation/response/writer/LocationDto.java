package site.travellaboratory.be.article.presentation.response.writer;

import site.travellaboratory.be.article.domain.Location;

public record LocationDto (
    String placeId,
    String address,
    String city
) {
    public static LocationDto from(Location location) {
        return new LocationDto(
            location.getPlaceId(),
            location.getAddress(),
            location.getCity()
        );
    }

    public Location toModel() {
        return Location.builder()
            .placeId(this.placeId)
            .address(this.address)
            .city(this.city)
            .build();
    }
}
