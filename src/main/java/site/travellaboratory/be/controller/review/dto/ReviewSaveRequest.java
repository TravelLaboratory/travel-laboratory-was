package site.travellaboratory.be.controller.review.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import site.travellaboratory.be.domain.review.ReviewStatus;

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
