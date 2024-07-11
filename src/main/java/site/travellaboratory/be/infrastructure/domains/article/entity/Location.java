package site.travellaboratory.be.infrastructure.domains.article.entity;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Location {

    private String placeId;

    private String address;

    private String city;
}
