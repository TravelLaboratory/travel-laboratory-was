package site.travellaboratory.be.article.presentation.response.writer;

import site.travellaboratory.be.article.domain.Location;

public record LocationResponse(
    String placeId,
    String address,
    String city
) {
    public static LocationResponse from(Location location) {
        return new LocationResponse(
            location.getPlaceId(),
            location.getAddress(),
            location.getCity()
        );
    }

//    public Location toModel() {
//        return Location.builder()
//            .placeId(this.placeId)
//            .address(this.address)
//            .city(this.city)
//            .build();
//    }
}
