package site.travellaboratory.be.controller.review.dto;

import jakarta.validation.constraints.NotBlank;

public record ReviewUpdateRequest(
    @NotBlank
    String title,
    String representativeImgUrl,
    @NotBlank
    String description
) {

}
