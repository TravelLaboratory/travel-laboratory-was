package site.travellaboratory.be.domain.article;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
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

    private Duration duration;

    private String title;

    private String imageUrl;

    private String expense;

    public Article(final Long id,
                   final User user,
                   final LocalDateTime startAt,
                   final LocalDateTime endAt,
                   final String title,
                   final String imageUrl,
                   final String expense
    ) {
        this.id = id;
        this.user = user;
        this.duration = new Duration(startAt, endAt);
        this.title = title;
        this.imageUrl = imageUrl;
        this.expense = expense;
    }

    public static Article of(final User user, final ArticleRegisterRequest articleRegisterRequest) {
        return new Article(null, user, articleRegisterRequest.startAt(), articleRegisterRequest.endAt(),
                articleRegisterRequest.title(),
                articleRegisterRequest.imageUrl(), articleRegisterRequest.expense());
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
