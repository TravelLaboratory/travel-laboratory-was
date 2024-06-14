package site.travellaboratory.be.controller.review.dto;

import jakarta.validation.constraints.NotBlank;

public record ReviewSaveRequest(
    @NotBlank
    String title,
    String representativeImgUrl,
    @NotBlank
    String description
) {
}
