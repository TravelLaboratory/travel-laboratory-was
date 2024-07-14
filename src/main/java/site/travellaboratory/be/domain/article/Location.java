package site.travellaboratory.be.domain.article;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class Location {
    private final String placeId;

    private final String address;

    private final String city;
}
