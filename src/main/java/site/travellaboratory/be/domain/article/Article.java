package site.travellaboratory.be.domain.article;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.domain.article.enums.ArticleStatus;
import site.travellaboratory.be.domain.article.enums.TravelCompanion;
import site.travellaboratory.be.domain.article.enums.TravelStyle;
import site.travellaboratory.be.domain.user.user.User;

@Getter
@Builder
@RequiredArgsConstructor
public class Article {
    private final Long id;
    private final User user;
    private final String title;
    @Builder.Default
    private final List<Location> locations = new ArrayList<>();
    private final LocalDate startAt;
    private final LocalDate endAt;
    private final String expense;
    // todo: 체크한번
    private final TravelCompanion travelCompanion;
    // todo: 체크한번
    @Builder.Default
    private final List<TravelStyle> travelStyles  = new ArrayList<>();
    private final ArticleStatus status;
    private final String coverImageUrl;

    public static Article create(
        User user,
        String title,
        List<Location> locations,
        LocalDate startAt,
        LocalDate endAt,
        String expense,
        TravelCompanion travelCompanion,
        List<TravelStyle> travelStyles
    ) {
        return Article.builder()
            .user(user)
            .title(title)
            .locations(locations)
            .startAt(startAt)
            .endAt(endAt)
            .expense(expense)
            .travelCompanion(travelCompanion)
            .travelStyles(travelStyles)
            .status(ArticleStatus.ACTIVE) // 기본 상태를 ACTIVE로 설정
            .coverImageUrl(null) // 여행 생성 시에는 coverImgUrl 받지 않음
            .build();
    }

    public Article update(
        User user,
        String title,
        List<Location> locations,
        LocalDate startAt,
        LocalDate endAt,
        String expense,
        TravelCompanion travelCompanion,
        List<TravelStyle> travelStyles
    ) {
        return Article.builder()
            .id(id)
            .user(user)
            .title(title)
            .locations(locations)
            .startAt(startAt)
            .endAt(endAt)
            .expense(expense)
            .travelCompanion(travelCompanion)
            .travelStyles(travelStyles)
            .status(status) // 기본 상태를 ACTIVE로 설정
            .coverImageUrl(coverImageUrl)
            .build();
    }

    public Article delete() {
        return Article.builder()
            .id(this.id)
            .user(this.user)
            .title(this.title)
            .locations(this.locations)
            .startAt(this.startAt)
            .endAt(this.endAt)
            .expense(this.expense)
            .travelCompanion(this.travelCompanion)
            .travelStyles(this.travelStyles)
            .status(ArticleStatus.INACTIVE)
            .coverImageUrl(this.coverImageUrl)
            .build();
    }

    public void verifyOwner(User user) {
        if (!this.user.getId().equals(user.getId())) {
            throw new BeApplicationException(ErrorCodes.REVIEW_POST_NOT_USER, HttpStatus.FORBIDDEN);
        }
    }
}


