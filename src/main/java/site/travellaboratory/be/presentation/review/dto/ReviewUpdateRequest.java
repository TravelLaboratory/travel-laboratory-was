package site.travellaboratory.be.presentation.review.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import site.travellaboratory.be.infrastructure.domains.review.enums.ReviewStatus;

public record ReviewUpdateRequest(
    @NotBlank
    String title,
    String representativeImgUrl,
    @NotBlank
    String description,
    @NotNull
    ReviewStatus status
) {

}
