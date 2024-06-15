package site.travellaboratory.be.controller.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentSaveRequest(
    @NotNull
    Long reviewId,
    @NotBlank
    String replyComment
) {
}
