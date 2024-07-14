package site.travellaboratory.be.domain.review;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.domain.review.enums.ReviewStatus;
import site.travellaboratory.be.infrastructure.domains.article.entity.ArticleJpaEntity;
import site.travellaboratory.be.infrastructure.domains.user.entity.UserJpaEntity;

@Getter
@Builder
@RequiredArgsConstructor
public class Review {

    private final Long id;
    private final UserJpaEntity userJpaEntity;
    private final ArticleJpaEntity articleJpaEntity;
    private final String title;
    private final String representativeImgUrl;
    private final String description;
    private final ReviewStatus status;

    public static Review create(UserJpaEntity userJpaEntity, ArticleJpaEntity articleJpaEntity, String title,
        String representativeImgUrl, String description, ReviewStatus status) {
        // 유저가 작성한 article_id이 아닌 경우
        articleJpaEntity.verifyOwner(userJpaEntity);

        return Review.builder()
            .userJpaEntity(userJpaEntity)
            .articleJpaEntity(articleJpaEntity)
            .title(title)
            .representativeImgUrl(representativeImgUrl)
            .description(description)
            // todo: 공개 비공개를 따로 만들어서 status 대신 공개여부를 받도록 수정
            .status(status)
            .build();
    }

    public Review withUpdatedContent(UserJpaEntity userJpaEntity, String title, String representativeImgUrl, String description, ReviewStatus status) {
        // 유저가 작성한 후기가 아닌 경우
        verifyOwner(userJpaEntity);

        return Review.builder()
            .id(this.id)
            .userJpaEntity(this.userJpaEntity)
            .articleJpaEntity(this.articleJpaEntity)
            .title(title)
            .representativeImgUrl(representativeImgUrl)
            .description(description)
            .status(status)
            .build();
    }

    public Review withInactiveStatus(UserJpaEntity userJpaEntity) {
        // 유저가 작성한 후기인지 확인
        verifyOwner(userJpaEntity);

        return Review.builder()
            .id(this.id)
            .userJpaEntity(this.userJpaEntity)
            .articleJpaEntity(this.articleJpaEntity)
            .title(this.title)
            .representativeImgUrl(this.representativeImgUrl)
            .description(this.description)
            .status(ReviewStatus.INACTIVE)
            .build();
    }

    private void verifyOwner(UserJpaEntity userJpaEntity) {
        // 유저가 작성한 후기가 아닌 경우
        if (!this.getUserJpaEntity().getId().equals(userJpaEntity.getId())) {
            throw new BeApplicationException(ErrorCodes.REVIEW_UPDATE_NOT_USER,
                HttpStatus.FORBIDDEN);
        }
    }
}
