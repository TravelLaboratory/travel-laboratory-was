package site.travellaboratory.be.article.domain.request;

import lombok.Builder;
import site.travellaboratory.be.article.domain.Location;


public record LocationRequest(
    String placeId,
    String address,
    String city
) {
    public Location toModel() {
        return Location.builder()
            .placeId(this.placeId)
            .address(this.address)
            .city(this.city)
            .build();
    }

    @Builder
    public LocationRequest {}
}
