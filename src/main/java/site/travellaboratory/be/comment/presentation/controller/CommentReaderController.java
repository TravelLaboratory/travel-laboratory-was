package site.travellaboratory.be.comment.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.comment.application.service.CommentReaderService;
import site.travellaboratory.be.comment.presentation.response.reader.CommentReadPaginationResponse;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.common.presentation.response.ApiResponse;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CommentReaderController {

    private final CommentReaderService commentReaderService;

    @GetMapping("/reviews/{reviewId}/comments")
    public ResponseEntity<ApiResponse<CommentReadPaginationResponse>> readAllCommentPagination(
        @UserId Long userId,
        @PathVariable(name = "reviewId") Long reviewId,
        @RequestParam(defaultValue = "0", name = "page") int page,
        @RequestParam(defaultValue = "10", name = "size") int size
    ) {
        CommentReadPaginationResponse response = commentReaderService.readCommentsPagination(
            userId, reviewId, page, size);
        return ResponseEntity.ok(ApiResponse.OK(response));
    }
}
