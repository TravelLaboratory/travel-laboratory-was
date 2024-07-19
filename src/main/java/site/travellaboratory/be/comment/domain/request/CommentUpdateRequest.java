package site.travellaboratory.be.comment.domain.request;

import lombok.Builder;

public record CommentUpdateRequest(
    String replyComment
) {
    @Builder
    public CommentUpdateRequest {
    }
}
