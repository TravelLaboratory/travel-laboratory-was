package site.travellaboratory.be.review.domain.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public record ReviewUpdateRequest(
    @NotBlank
    String title,
    String representativeImgUrl,
    @NotBlank
    String description
) {
    @Builder
    public ReviewUpdateRequest {
    }
}
