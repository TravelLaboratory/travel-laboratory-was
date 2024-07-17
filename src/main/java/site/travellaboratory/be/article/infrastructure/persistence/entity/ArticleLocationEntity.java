package site.travellaboratory.be.article.infrastructure.persistence.entity;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.travellaboratory.be.article.domain.Location;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ArticleLocationEntity {

    private String placeId;

    private String address;

    private String city;

    public static ArticleLocationEntity from(Location location) {
        ArticleLocationEntity result = new ArticleLocationEntity();
        result.placeId = location.getPlaceId();
        result.address = location.getAddress();
        result.city = location.getCity();
        return result;
    }

    public Location toModel() {
        return Location.builder()
            .placeId(this.placeId)
            .address(this.address)
            .city(this.city)
            .build();
    }
}
