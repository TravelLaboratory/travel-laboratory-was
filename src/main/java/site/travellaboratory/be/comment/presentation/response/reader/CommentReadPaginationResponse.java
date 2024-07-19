package site.travellaboratory.be.comment.presentation.response.reader;

import java.util.List;
import org.springframework.data.domain.Page;
import site.travellaboratory.be.comment.infrastructure.persistence.entity.CommentEntity;

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
        Page<CommentEntity> commentPage
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
