package site.travellaboratory.be.review.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
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
    @Builder
    public ReviewSaveRequest {
    }
}
