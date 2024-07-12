package site.travellaboratory.be.infrastructure.domains.article.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.infrastructure.domains.article.converter.TravelCompanionConverter;
import site.travellaboratory.be.infrastructure.domains.article.converter.TravelStyleConverter;
import site.travellaboratory.be.infrastructure.domains.article.enums.ArticleStatus;
import site.travellaboratory.be.infrastructure.domains.article.enums.TravelCompanion;
import site.travellaboratory.be.infrastructure.domains.article.enums.TravelStyle;
import site.travellaboratory.be.infrastructure.domains.user.entity.User;
import site.travellaboratory.be.presentation.article.dto.writer.ArticleRegisterRequest;
import site.travellaboratory.be.presentation.article.dto.writer.ArticleUpdateRequest;
import site.travellaboratory.be.infrastructure.common.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    @ElementCollection
    @CollectionTable(name = "article_location", joinColumns = @JoinColumn(name = "article_id"))
    private List<Location> location = new ArrayList<>();

    private LocalDate startAt;

    private LocalDate endAt;

    private String expense;

    @Convert(converter = TravelCompanionConverter.class)
    private TravelCompanion travelCompanion;

    @ElementCollection
    @CollectionTable(name = "article_travel_styles", joinColumns = @JoinColumn(name = "article_id"))
    @Convert(converter = TravelStyleConverter.class)
    private List<TravelStyle> travelStyles = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ArticleStatus status;

    private String coverImageUrl;

    public Article(final Long id,
                   final User user,
                   final String title,
                   final List<Location> location,
                   final LocalDate startAt,
                   final LocalDate endAt,
                   final String expense,
                   final String travelCompanion,
                   final List<String> travelStyles
    ) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.location = location;
        this.startAt = startAt;
        this.endAt = endAt;
        this.expense = expense;
        this.travelCompanion = TravelCompanion.from(travelCompanion);
        this.travelStyles = TravelStyle.from(travelStyles);
        this.status = ArticleStatus.ACTIVE;
    }

    public static Article of(final User user, final ArticleRegisterRequest articleRegisterRequest) {
        return new Article(
                null,
                user,
                articleRegisterRequest.title(),
                articleRegisterRequest.location(),
                articleRegisterRequest.startAt(),
                articleRegisterRequest.endAt(),
                articleRegisterRequest.expense(),
                articleRegisterRequest.travelCompanion(),
                articleRegisterRequest.travelStyles()
        );
    }

    public void update(final ArticleUpdateRequest articleUpdateRequest) {
        this.title = articleUpdateRequest.title();
        this.location = articleUpdateRequest.location();
        this.startAt = articleUpdateRequest.startAt();
        this.endAt = articleUpdateRequest.endAt();
        this.expense = articleUpdateRequest.expense();
        this.travelCompanion = TravelCompanion.from(articleUpdateRequest.travelCompanion());
        this.travelStyles = TravelStyle.from(articleUpdateRequest.travelStyles());
    }

    public void updateCoverImage(final String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    // 지우지 마세요!!! [상권] - 초기 여행 계획 + 일정 리스트 삭제 시 사용중
    public void delete() {
        this.status = ArticleStatus.INACTIVE;
    }

    // 지우지 마세요!! 일정 상세 - 공개, 비공개 설정
    public void togglePrivacyStatus() {
        if (this.status == ArticleStatus.ACTIVE) {
            this.status = ArticleStatus.PRIVATE;
        } else if (this.status == ArticleStatus.PRIVATE) {
            this.status = ArticleStatus.ACTIVE;
        }
    }

    public void updateStatus(ArticleStatus status) {
        this.status = status;
    }

    public String getNickname() {
        return user.getNickname();
    }

    public void verifyOwner(User user) {
        if (!this.user.getId().equals(user.getId())) {
            throw new BeApplicationException(ErrorCodes.REVIEW_POST_NOT_USER, HttpStatus.FORBIDDEN);
        }
    }
}
