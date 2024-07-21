package site.travellaboratory.be.review.presentation.response.reader;

import java.time.format.DateTimeFormatter;
import site.travellaboratory.be.review.infrastructure.persistence.entity.ReviewEntity;

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
    public static ReviewReadDetailResponse from(ReviewEntity reviewEntity,
        boolean isEditable,boolean isLike,long likeCount
        ) {
        return new ReviewReadDetailResponse(
            reviewEntity.getUserEntity().getId(),
            reviewEntity.getUserEntity().getProfileImgUrl(),
            reviewEntity.getUserEntity().getNickname(),
            isEditable,
            reviewEntity.getArticleEntity().getId(),
            reviewEntity.getId(),
            reviewEntity.getTitle(),
            reviewEntity.getRepresentativeImgUrl(),
            reviewEntity.getDescription(),
            reviewEntity.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
            isLike,
            likeCount
            );
    }
}
