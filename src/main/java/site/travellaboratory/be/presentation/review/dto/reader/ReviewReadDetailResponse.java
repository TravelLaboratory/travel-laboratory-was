package site.travellaboratory.be.presentation.review.dto.reader;

import java.time.format.DateTimeFormatter;
import site.travellaboratory.be.infrastructure.domains.review.entity.ReviewJpaEntity;

public record ReviewReadDetailResponse(
    Long userId,
    String profileImgUrl,
    String nickname,
    boolean isEditable,
    Long articleId,
    Long reviewId,
    String title,
    String representativeImgUrl,
    String description,
    String createdAt,
    boolean isLike,
    long likeCount
) {
    public static ReviewReadDetailResponse from(ReviewJpaEntity reviewJpaEntity,
        boolean isEditable,boolean isLike,long likeCount
        ) {
        return new ReviewReadDetailResponse(
            reviewJpaEntity.getUser().getId(),
            reviewJpaEntity.getUser().getProfileImgUrl(),
            reviewJpaEntity.getUser().getNickname(),
            isEditable,
            reviewJpaEntity.getArticle().getId(),
            reviewJpaEntity.getId(),
            reviewJpaEntity.getTitle(),
            reviewJpaEntity.getRepresentativeImgUrl(),
            reviewJpaEntity.getDescription(),
            reviewJpaEntity.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
            isLike,
            likeCount
            );
    }
}
