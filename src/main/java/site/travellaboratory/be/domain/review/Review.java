package site.travellaboratory.be.domain.review;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.domain.review.enums.ReviewStatus;
import site.travellaboratory.be.infrastructure.domains.article.entity.Article;
import site.travellaboratory.be.infrastructure.domains.user.entity.User;

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
        article.verifyOwner(user.getId());

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

    public Review withUpdatedContent(Long userId, String title, String representativeImgUrl, String description, ReviewStatus status) {
        // 유저가 작성한 후기가 아닌 경우
        verifyOwner(userId);

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

    public Review withInactiveStatus(Long userId) {
        // 유저가 작성한 후기인지 확인
        verifyOwner(userId);

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

    private void verifyOwner(Long userId) {
        // 유저가 작성한 후기가 아닌 경우
        if (!this.getUser().getId().equals(userId)) {
            throw new BeApplicationException(ErrorCodes.REVIEW_UPDATE_NOT_USER,
                HttpStatus.FORBIDDEN);
        }
    }
}
