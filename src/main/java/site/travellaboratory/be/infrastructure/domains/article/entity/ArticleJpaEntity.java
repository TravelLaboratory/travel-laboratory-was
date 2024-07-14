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
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.travellaboratory.be.domain.article.Article;
import site.travellaboratory.be.domain.article.enums.ArticleStatus;
import site.travellaboratory.be.domain.article.enums.TravelCompanion;
import site.travellaboratory.be.domain.article.enums.TravelStyle;
import site.travellaboratory.be.infrastructure.common.BaseEntity;
import site.travellaboratory.be.infrastructure.domains.article.converter.TravelCompanionConverter;
import site.travellaboratory.be.infrastructure.domains.article.converter.TravelStyleConverter;
import site.travellaboratory.be.infrastructure.domains.user.entity.UserJpaEntity;

@Entity
@Table(name = "article")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserJpaEntity userJpaEntity;

    private String title;

    @ElementCollection
    @CollectionTable(name = "article_location", joinColumns = @JoinColumn(name = "article_id"))
    private List<ArticleLocationJpaEntity> locationJpaEntities = new ArrayList<>();

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

    public static ArticleJpaEntity from(Article article) {
        ArticleJpaEntity result = new ArticleJpaEntity();
        result.id = article.getId();
        result.userJpaEntity = UserJpaEntity.from(article.getUser());
        result.title = article.getTitle();
        result.locationJpaEntities = article.getLocations().stream()
            .map(ArticleLocationJpaEntity::from)
            .toList();
        result.startAt = article.getStartAt();
        result.endAt = article.getEndAt();
        result.expense = article.getExpense();
        result.travelCompanion = article.getTravelCompanion();
        result.travelStyles = article.getTravelStyles();
        result.status = article.getStatus();
        result.coverImageUrl = article.getCoverImageUrl();
        return result;
    }

    public Article toModel() {
        return Article.builder()
            .id(this.id)
            .user(this.userJpaEntity.toModel())
            .title(this.title)
            .locations(this.locationJpaEntities.stream()
                .map(ArticleLocationJpaEntity::toModel).toList())
            .startAt(this.startAt)
            .endAt(this.endAt)
            .expense(this.expense)
            .travelCompanion(this.travelCompanion)
            .travelStyles(this.travelStyles)
            .status(this.status)
            .coverImageUrl(this.coverImageUrl)
            .build();
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
        return userJpaEntity.getNickname();
    }
}
