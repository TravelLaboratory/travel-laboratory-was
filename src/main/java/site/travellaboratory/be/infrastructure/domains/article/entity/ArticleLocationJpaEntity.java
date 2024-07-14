package site.travellaboratory.be.infrastructure.domains.article.entity;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.travellaboratory.be.domain.article.Location;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ArticleLocationJpaEntity {
    private String placeId;

    private String address;

    private String city;

    public static ArticleLocationJpaEntity from(Location location) {
        ArticleLocationJpaEntity result = new ArticleLocationJpaEntity();
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
