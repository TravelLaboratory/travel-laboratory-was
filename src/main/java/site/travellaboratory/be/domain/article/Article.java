package site.travellaboratory.be.domain.article;

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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.travellaboratory.be.config.TravelCompanionConverter;
import site.travellaboratory.be.config.TravelStyleConverter;
import site.travellaboratory.be.controller.article.dto.ArticleRegisterRequest;
import site.travellaboratory.be.controller.article.dto.ArticleUpdateRequest;
import site.travellaboratory.be.domain.BaseEntity;
import site.travellaboratory.be.domain.user.entity.User;

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
    @CollectionTable(name = "article_locations", joinColumns = @JoinColumn(name = "article_id"))
    private List<Location> location = new ArrayList<>();

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private String expense;

    @Convert(converter = TravelCompanionConverter.class)
    private TravelCompanion travelCompanion;

    @ElementCollection
    @CollectionTable(name = "article_travel_styles", joinColumns = @JoinColumn(name = "article_id"))
    @Convert(converter = TravelStyleConverter.class)
    private List<TravelStyle> travelStyles = new ArrayList<>();

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private ArticleStatus status;

    public Article(final Long id,
                   final User user,
                   final String title,
                   final List<Location> location,
                   final LocalDateTime startAt,
                   final LocalDateTime endAt,
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
                articleRegisterRequest.style()
        );
    }

    public void update(final ArticleUpdateRequest articleUpdateRequest) {
        this.title = articleUpdateRequest.title();
        this.location = articleUpdateRequest.location();
        this.startAt = articleUpdateRequest.startAt();
        this.endAt = articleUpdateRequest.endAt();
        this.expense = articleUpdateRequest.expense();
        this.travelCompanion = TravelCompanion.from(articleUpdateRequest.travelCompanion());
        this.travelStyles = TravelStyle.from(articleUpdateRequest.style());
    }

    public void toggleStatus() {
        if (this.status == ArticleStatus.ACTIVE) {
            this.status = ArticleStatus.PRIVATE;
        } else if (this.status == ArticleStatus.PRIVATE) {
            this.status = ArticleStatus.ACTIVE;
        }
    }

    public void delete() {
        this.status = ArticleStatus.INACTIVE;
    }

    public String getNickname() {
        return user.getNickname();
    }
}
