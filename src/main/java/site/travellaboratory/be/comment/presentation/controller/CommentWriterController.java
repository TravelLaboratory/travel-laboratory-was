package site.travellaboratory.be.comment.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.comment.application.service.CommentWriterService;
import site.travellaboratory.be.comment.domain.Comment;
import site.travellaboratory.be.comment.presentation.response.writer.CommentDeleteResponse;
import site.travellaboratory.be.comment.domain.request.CommentSaveRequest;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.comment.presentation.response.writer.CommentSaveResponse;
import site.travellaboratory.be.comment.domain.request.CommentUpdateRequest;
import site.travellaboratory.be.comment.presentation.response.writer.CommentUpdateResponse;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CommentWriterController {

    private final CommentWriterService commentWriterService;

    @PostMapping("/comment")
    public ResponseEntity<CommentSaveResponse> saveComment(
        @UserId Long userId,
        @Valid @RequestBody CommentSaveRequest commentSaveRequest
    ) {
        Comment result = commentWriterService.save(userId, commentSaveRequest);
        return new ResponseEntity<>(CommentSaveResponse.from(result), HttpStatus.CREATED);
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CommentUpdateResponse> updateComment(
        @UserId Long userId,
        @PathVariable(name = "commentId") Long commentId,
        @Valid @RequestBody CommentUpdateRequest commentUpdateRequest
    ) {
        Comment result = commentWriterService.update(userId, commentId, commentUpdateRequest);
        return ResponseEntity.ok(CommentUpdateResponse.from(result));
    }

    @PatchMapping("/comments/{commentId}/status")
    public ResponseEntity<CommentDeleteResponse> deleteComment(
        @UserId Long userId,
        @PathVariable(name = "commentId") Long commentId
    ) {
        Comment result = commentWriterService.delete(userId, commentId);
        return ResponseEntity.ok(CommentDeleteResponse.from(result));
    }
}
