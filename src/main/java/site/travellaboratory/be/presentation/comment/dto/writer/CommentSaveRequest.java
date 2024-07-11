package site.travellaboratory.be.presentation.comment.dto.writer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentSaveRequest(
    @NotNull
    Long reviewId,
    @NotBlank
    String replyComment
) {
}