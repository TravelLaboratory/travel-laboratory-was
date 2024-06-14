package site.travellaboratory.be.domain.article;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import site.travellaboratory.be.controller.article.dto.ArticleRegisterRequest;
import site.travellaboratory.be.domain.BaseEntity;
import site.travellaboratory.be.domain.user.entity.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    @ElementCollection
    @CollectionTable(name = "article_locations", joinColumns = @JoinColumn(name = "article_id"))
    @Column(name = "location")
    private List<String> location = new ArrayList<>();

    private Duration duration;

    private String expense;

    @ElementCollection
    @CollectionTable(name = "article_travel_companions", joinColumns = @JoinColumn(name = "article_id"))
    private List<String> travelCompanions = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "article_travel_styles", joinColumns = @JoinColumn(name = "article_id"))
    private List<String> travelStyles = new ArrayList<>();

    private String imageUrl;
  
    @Enumerated(EnumType.STRING)
    private ArticleStatus status;

    public Article(final Long id,
                   final User user,
                   final String title,
                   final List<String> location,
                   final LocalDateTime startAt,
                   final LocalDateTime endAt,
                   final String expense,
                   final List<String> travelCompanions,
                   final List<String> travelStyles
    ) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.location = location;
        this.duration = new Duration(startAt, endAt);
        this.expense = expense;
        this.travelCompanions = travelCompanions;
        this.travelStyles = travelStyles;
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

    public String getNickname() {
        return user.getNickname();
    }

    public LocalDateTime getStartAt() {
        return duration.getStartAt();
    }

    public LocalDateTime getEndAt() {
        return duration.getEndAt();
    }
}
