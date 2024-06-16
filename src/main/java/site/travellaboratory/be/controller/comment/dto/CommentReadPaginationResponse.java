package site.travellaboratory.be.controller.comment.dto;

import java.util.List;
import org.springframework.data.domain.Page;
import site.travellaboratory.be.domain.comment.Comment;

public record CommentReadPaginationResponse(
    List<CommentReadResponse> comments,
    int currentPage,
    int pageSize,
    int totalPages,
    long totalComments
) {

    public static CommentReadPaginationResponse from(
        List<CommentReadResponse> comments,
        Page<Comment> commentPage
    ) {
        return new CommentReadPaginationResponse(
            comments,
            commentPage.getNumber(),
            commentPage.getSize(),
            commentPage.getTotalPages(),
            commentPage.getTotalElements()
        );
    }
}
