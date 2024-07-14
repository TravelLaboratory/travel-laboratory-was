package site.travellaboratory.be.presentation.article.dto.writer;

import site.travellaboratory.be.domain.article.Location;

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
