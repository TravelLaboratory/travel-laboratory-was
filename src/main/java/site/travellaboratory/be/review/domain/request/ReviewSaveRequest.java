package site.travellaboratory.be.review.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public record ReviewSaveRequest(
    @NotNull
    Long articleId,
    @NotBlank
    String title,
    String representativeImgUrl,
    @NotBlank
    String description
) {
    @Builder
    public ReviewSaveRequest {
    }
}
