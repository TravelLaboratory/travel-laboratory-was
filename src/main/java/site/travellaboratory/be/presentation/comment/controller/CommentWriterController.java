package site.travellaboratory.be.presentation.comment.controller;

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
import site.travellaboratory.be.application.comment.CommentWriterService;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.presentation.comment.dto.writer.CommentDeleteResponse;
import site.travellaboratory.be.presentation.comment.dto.writer.CommentSaveRequest;
import site.travellaboratory.be.presentation.comment.dto.writer.CommentSaveResponse;
import site.travellaboratory.be.presentation.comment.dto.writer.CommentUpdateRequest;
import site.travellaboratory.be.presentation.comment.dto.writer.CommentUpdateResponse;

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
        CommentSaveResponse response = commentWriterService.saveComment(userId, commentSaveRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CommentUpdateResponse> updateComment(
        @UserId Long userId,
        @PathVariable(name = "commentId") Long commentId,
        @Valid @RequestBody CommentUpdateRequest commentUpdateRequest
    ) {
        CommentUpdateResponse response = commentWriterService.updateComment(userId, commentId, commentUpdateRequest);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/comments/{commentId}/status")
    public ResponseEntity<CommentDeleteResponse> deleteComment(
        @UserId Long userId,
        @PathVariable(name = "commentId") Long commentId
    ) {
        CommentDeleteResponse response = commentWriterService.deleteComment(userId, commentId);
        return ResponseEntity.ok(response);
    }
}