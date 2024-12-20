package site.travellaboratory.be.article.domain.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import site.travellaboratory.be.common.presentation.exception.BeApplicationException;
import site.travellaboratory.be.common.presentation.error.ErrorCodes;

public enum TravelStyle {
    HOCANCE("호캉스"),
    HEALING("힐링"),
    WALK("뚜벅이"),
    NONSTOP_TOUR("쉴 틈 없이 관광"),
    CAFE_TOUR("카페 투어"),
    FOOD_TOUR("맛집 탐방"),
    WITH_NATURE("자연과 함께"),
    SHOPPING_LOVER("쇼핑 러버"),
    ACTIVITY("액티비티"),
    HOT_PLACE("핫플레이스"),
    PHOTO_SPOT("남는 건 사진");

    private final String name;

    TravelStyle(String name) {
        this.name = name;
    }

    public static List<TravelStyle> from(List<String> names) {
        return names == null ? List.of() : names.stream()
                .map(TravelStyle::fromDbValue)
                .collect(Collectors.toList());
    }

    public static TravelStyle fromDbValue(String dbValue) {
        return Arrays.stream(TravelStyle.values())
                .filter(value -> value.name.equals(dbValue))
                .findFirst()
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.STYLE_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    public String getName() {
        return name;
    }
}
