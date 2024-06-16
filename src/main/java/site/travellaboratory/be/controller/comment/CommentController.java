package site.travellaboratory.be.controller.comment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.controller.comment.dto.CommentDeleteResponse;
import site.travellaboratory.be.controller.comment.dto.CommentReadPaginationResponse;
import site.travellaboratory.be.controller.comment.dto.CommentSaveRequest;
import site.travellaboratory.be.controller.comment.dto.CommentSaveResponse;
import site.travellaboratory.be.controller.comment.dto.CommentUpdateRequest;
import site.travellaboratory.be.controller.comment.dto.CommentUpdateResponse;
import site.travellaboratory.be.controller.comment.dto.userlikecomment.CommentToggleLikeResponse;
import site.travellaboratory.be.service.CommentService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/reviews/{reviewId}/comments")
    public ResponseEntity<CommentReadPaginationResponse> readAllCommentPagination(
        @UserId Long userId,
        @PathVariable(name = "reviewId") Long reviewId,
        @RequestParam(defaultValue = "0", name = "page") int page,
        @RequestParam(defaultValue = "10", name = "size") int size
    ) {
        CommentReadPaginationResponse response = commentService.readAllCommentPagination(
            userId, reviewId, page, size);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/comment")
    public ResponseEntity<CommentSaveResponse> saveComment(
        @UserId Long userId,
        @Valid @RequestBody CommentSaveRequest commentSaveRequest
    ) {
        CommentSaveResponse response = commentService.saveComment(userId, commentSaveRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CommentUpdateResponse> updateComment(
        @UserId Long userId,
        @PathVariable(name = "commentId") Long commentId,
        @Valid @RequestBody CommentUpdateRequest commentUpdateRequest
    ) {
        CommentUpdateResponse response = commentService.updateComment(userId, commentId, commentUpdateRequest);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/comments/{commentId}/status")
    public ResponseEntity<CommentDeleteResponse> deleteComment(
        @UserId Long userId,
        @PathVariable(name = "commentId") Long commentId
    ) {
        CommentDeleteResponse response = commentService.deleteComment(userId, commentId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/comments/{commentId}/likes")
    public ResponseEntity<CommentToggleLikeResponse> toggleLikeComment(
        @UserId Long userId,
        @PathVariable(name = "commentId") Long commentId
    ) {
        CommentToggleLikeResponse response = commentService.toggleLikeComment(userId,
            commentId);
        return ResponseEntity.ok(response);
    }
}
