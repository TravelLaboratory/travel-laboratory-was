package site.travellaboratory.be.controller.comment.dto;

import java.util.List;
import org.springframework.data.domain.Page;
import site.travellaboratory.be.domain.comment.Comment;

public record CommentReadPaginationResponse(
    String profileImgUrl,
    List<CommentReadResponse> comments,
    int currentPage,
    int pageSize,
    int totalPages,
    long totalComments
) {

    public static CommentReadPaginationResponse from(
        String profileImgUrl,
        List<CommentReadResponse> comments,
        Page<Comment> commentPage
    ) {
        return new CommentReadPaginationResponse(
            profileImgUrl,
            comments,
            commentPage.getNumber(),
            commentPage.getSize(),
            commentPage.getTotalPages(),
            commentPage.getTotalElements()
        );
    }
}
