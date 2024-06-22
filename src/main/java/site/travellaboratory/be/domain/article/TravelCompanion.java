package site.travellaboratory.be.domain.article;

import java.util.Arrays;
import org.springframework.http.HttpStatus;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;

public enum TravelCompanion {
    FAMILY("가족들과"),
    FRIENDS("친구와"),
    CHILD("아이와"),
    PARTNER("연인과"),
    PARENTS("부모님과"),
    ACTOR("배우자와"),
    ALONE("혼자서"),
    COLLEAGUES("동료와"),
    ETC("기타");


    private final String name;

    TravelCompanion(String name) {
        this.name = name;
    }

    public static TravelCompanion from(String name) {
        return Arrays.stream(TravelCompanion.values())
                .filter(value -> value.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.COMPANION_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    public String getName() {
        return name;
    }
}
