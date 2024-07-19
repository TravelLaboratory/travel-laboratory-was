package site.travellaboratory.be.comment.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public record CommentSaveRequest(
    @NotNull
    Long reviewId,
    @NotBlank
    String replyComment
) {
    @Builder
    public CommentSaveRequest {
    }
}
