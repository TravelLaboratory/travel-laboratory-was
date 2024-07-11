package site.travellaboratory.be.presentation.review.dto;

import java.time.format.DateTimeFormatter;
import site.travellaboratory.be.infrastructure.domains.review.entity.Review;

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
    public static ReviewReadDetailResponse from(Review review,
        boolean isEditable,boolean isLike,long likeCount
        ) {
        return new ReviewReadDetailResponse(
            review.getUser().getId(),
            review.getUser().getProfileImgUrl(),
            review.getUser().getNickname(),
            isEditable,
            review.getArticle().getId(),
            review.getId(),
            review.getTitle(),
            review.getRepresentativeImgUrl(),
            review.getDescription(),
            review.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
            isLike,
            likeCount
            );
    }
}
