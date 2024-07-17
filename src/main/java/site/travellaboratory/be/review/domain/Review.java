package site.travellaboratory.be.review.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.article.domain.Article;
import site.travellaboratory.be.review.domain.enums.ReviewStatus;
import site.travellaboratory.be.user.domain.User;

@Getter
@Builder
@RequiredArgsConstructor
public class Review {

    private final Long id;
    private final User user;
    private final Article article;
    private final String title;
    private final String representativeImgUrl;
    private final String description;
    private final ReviewStatus status;

    public static Review create(User user, Article article, String title,
        String representativeImgUrl, String description, ReviewStatus status) {
        // 유저가 작성한 article_id이 아닌 경우
        article.verifyOwner(user);

        return Review.builder()
            .user(user)
            .article(article)
            .title(title)
            .representativeImgUrl(representativeImgUrl)
            .description(description)
            // todo: 공개 비공개를 따로 만들어서 status 대신 공개여부를 받도록 수정
            .status(status)
            .build();
    }

    public Review withUpdatedContent(User user, String title, String representativeImgUrl, String description, ReviewStatus status) {
        // 유저가 작성한 후기가 아닌 경우
        verifyOwner(user);

        return Review.builder()
            .id(this.id)
            .user(this.user)
            .article(this.article)
            .title(title)
            .representativeImgUrl(representativeImgUrl)
            .description(description)
            .status(status)
            .build();
    }

    public Review withInactiveStatus(User user) {
        // 유저가 작성한 후기인지 확인
        verifyOwner(user);

        return Review.builder()
            .id(this.id)
            .user(this.user)
            .article(this.article)
            .title(this.title)
            .representativeImgUrl(this.representativeImgUrl)
            .description(this.description)
            .status(ReviewStatus.INACTIVE)
            .build();
    }

    private void verifyOwner(User user) {
        // 유저가 작성한 후기가 아닌 경우
        if (!this.getUser().getId().equals(user.getId())) {
            throw new BeApplicationException(ErrorCodes.REVIEW_UPDATE_NOT_USER,
                HttpStatus.FORBIDDEN);
        }
    }
}
