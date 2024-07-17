package site.travellaboratory.be.review.presentation.response.writer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import site.travellaboratory.be.review.domain.enums.ReviewStatus;

public record ReviewSaveRequest(
    @NotNull
    Long articleId,
    @NotBlank
    String title,
    String representativeImgUrl,
    @NotBlank
    String description,
    @NotNull
    ReviewStatus status
) {
}
