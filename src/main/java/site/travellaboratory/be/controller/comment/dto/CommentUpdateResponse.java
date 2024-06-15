package site.travellaboratory.be.controller.comment.dto;

public record CommentUpdateResponse(
    Long commentId
) {

    public static CommentUpdateResponse from(Long commentId) {
        return new CommentUpdateResponse(commentId);
    }
}
