package site.travellaboratory.be.review.domain;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import site.travellaboratory.be.article.domain.Article;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.error.ErrorCodes;
import site.travellaboratory.be.review.domain.enums.ReviewStatus;
import site.travellaboratory.be.review.domain.request.ReviewSaveRequest;
import site.travellaboratory.be.review.domain.request.ReviewUpdateRequest;
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
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static Review create(User user, Article article, ReviewSaveRequest saveRequest) {
        // 유저가 작성한 article_id이 아닌 경우
        article.verifyOwner(user);

        return Review.builder()
            .user(user)
            .article(article)
            .title(saveRequest.title())
            .representativeImgUrl(saveRequest.representativeImgUrl())
            .description(saveRequest.description())
            .status(ReviewStatus.ACTIVE)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    public Review withUpdatedContent(User user, ReviewUpdateRequest updatedContent) {
        // 유저가 작성한 후기가 아닌 경우
        verifyOwner(user);

        return Review.builder()
            .id(this.id)
            .user(this.user)
            .article(this.article)
            .title(updatedContent.title())
            .representativeImgUrl(updatedContent.representativeImgUrl())
            .description(updatedContent.description())
            .status(this.getStatus())
            .createdAt(this.createdAt)
            .updatedAt(LocalDateTime.now())
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
            .status(ReviewStatus.INACTIVE) // ACTIVE -> INACTIVE
            .createdAt(this.createdAt)
            .updatedAt(LocalDateTime.now())
            .build();
    }

    private void verifyOwner(User user) {
        // 유저가 작성한 후기가 아닌 경우
        if (!this.getUser().getId().equals(user.getId())) {
            throw new BeApplicationException(ErrorCodes.REVIEW_VERIFY_OWNER,
                HttpStatus.FORBIDDEN);
        }
    }
}
