package site.travellaboratory.be.comment.presentation.response.writer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentSaveRequest(
    @NotNull
    Long reviewId,
    @NotBlank
    String replyComment
) {
}
