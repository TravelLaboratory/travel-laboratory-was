package site.travellaboratory.be.article.infrastructure.persistence.entity;

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
import site.travellaboratory.be.article.domain.Article;
import site.travellaboratory.be.article.domain.enums.ArticleStatus;
import site.travellaboratory.be.article.domain.enums.TravelCompanion;
import site.travellaboratory.be.article.domain.enums.TravelStyle;
import site.travellaboratory.be.common.infrastructure.common.BaseEntity;
import site.travellaboratory.be.article.infrastructure.persistence.converter.TravelCompanionConverter;
import site.travellaboratory.be.article.infrastructure.persistence.converter.TravelStyleConverter;
import site.travellaboratory.be.user.infrastructure.persistence.entity.UserEntity;

@Entity
@Table(name = "article")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    private String title;

    @ElementCollection
    @CollectionTable(name = "article_location", joinColumns = @JoinColumn(name = "article_id"))
    private List<ArticleLocationEntity> locationEntities = new ArrayList<>();

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

    private String coverImgUrl;

    public static ArticleEntity from(Article article) {
        ArticleEntity result = new ArticleEntity();
        result.id = article.getId();
        result.userEntity = UserEntity.from(article.getUser());
        result.title = article.getTitle();
        result.locationEntities = article.getLocations().stream()
            .map(ArticleLocationEntity::from)
            .toList();
        result.startAt = article.getStartAt();
        result.endAt = article.getEndAt();
        result.expense = article.getExpense();
        result.travelCompanion = article.getTravelCompanion();
        result.travelStyles = article.getTravelStyles();
        result.status = article.getStatus();
        result.coverImgUrl = article.getCoverImgUrl();
        return result;
    }

    public Article toModel() {
        return Article.builder()
            .id(this.id)
            .user(this.userEntity.toModel())
            .title(this.title)
            .locations(this.locationEntities.stream()
                .map(ArticleLocationEntity::toModel).toList())
            .startAt(this.startAt)
            .endAt(this.endAt)
            .expense(this.expense)
            .travelCompanion(this.travelCompanion)
            .travelStyles(this.travelStyles)
            .status(this.status)
            .coverImgUrl(this.coverImgUrl)
            .build();
    }

    public void updateCoverImage(final String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
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
        return userEntity.getNickname();
    }
}
