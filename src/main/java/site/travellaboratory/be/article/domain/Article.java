package site.travellaboratory.be.article.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import site.travellaboratory.be.article.domain.enums.ArticleStatus;
import site.travellaboratory.be.article.domain.enums.TravelCompanion;
import site.travellaboratory.be.article.domain.enums.TravelStyle;
import site.travellaboratory.be.article.domain.request.ArticleRegisterRequest;
import site.travellaboratory.be.article.domain.request.ArticleUpdateRequest;
import site.travellaboratory.be.article.domain.request.LocationRequest;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.user.domain.User;

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
    private final String coverImgUrl;

    public static Article create(User user, ArticleRegisterRequest registerRequest) {
        return Article.builder()
            .user(user)
            .title(registerRequest.title())
            .locations(registerRequest.locations().stream().map(LocationRequest::toModel).toList())
            .startAt(registerRequest.startAt())
            .endAt(registerRequest.endAt())
            .expense(registerRequest.expense())
            .travelCompanion(TravelCompanion.from(registerRequest.travelCompanion()))
            .travelStyles(TravelStyle.from(registerRequest.travelStyles()))
            .status(ArticleStatus.ACTIVE) // 기본 상태를 ACTIVE로 설정
            .coverImgUrl(null) // 여행 생성 시에는 coverImgUrl 받지 않음
            .build();
    }

    public Article update(User user, ArticleUpdateRequest updateRequest) {
        verifyOwner(user);

        return Article.builder()
            .id(this.id)
            .user(this.user)
            .title(updateRequest.title())
            .locations(updateRequest.locations().stream().map(LocationRequest::toModel).toList())
            .startAt(updateRequest.startAt())
            .endAt(updateRequest.endAt())
            .expense(updateRequest.expense())
            .travelCompanion(TravelCompanion.from(updateRequest.travelCompanion()))
            .travelStyles(TravelStyle.from(updateRequest.travelStyles()))
            .status(this.status) // 기본 상태를 ACTIVE로 설정
            .coverImgUrl(this.coverImgUrl)
            .build();
    }

    public Article delete(User user) {
        verifyOwner(user);

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
            .coverImgUrl(this.coverImgUrl)
            .build();
    }

    public void verifyOwner(User user) {
        if (!this.user.getId().equals(user.getId())) {
            throw new BeApplicationException(ErrorCodes.ARTICLE_VERIFY_OWNER, HttpStatus.FORBIDDEN);
        }
    }
}


