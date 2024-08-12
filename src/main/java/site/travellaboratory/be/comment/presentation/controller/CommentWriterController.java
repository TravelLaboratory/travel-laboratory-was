package site.travellaboratory.be.comment.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.comment.application.service.CommentWriterService;
import site.travellaboratory.be.comment.domain.Comment;
import site.travellaboratory.be.comment.domain.request.CommentSaveRequest;
import site.travellaboratory.be.comment.domain.request.CommentUpdateRequest;
import site.travellaboratory.be.comment.presentation.response.writer.CommentDeleteResponse;
import site.travellaboratory.be.comment.presentation.response.writer.CommentSaveResponse;
import site.travellaboratory.be.comment.presentation.response.writer.CommentUpdateResponse;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.common.presentation.response.ApiResponse;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CommentWriterController {

    private final CommentWriterService commentWriterService;

    @PostMapping("/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CommentSaveResponse> save(
        @UserId Long userId,
        @Valid @RequestBody CommentSaveRequest commentSaveRequest
    ) {
        Comment result = commentWriterService.save(userId, commentSaveRequest);
        return ApiResponse.OK(CommentSaveResponse.from(result));
    }

    @PatchMapping("/comments/{commentId}")
    public ApiResponse<CommentUpdateResponse> update(
        @UserId Long userId,
        @PathVariable(name = "commentId") Long commentId,
        @Valid @RequestBody CommentUpdateRequest commentUpdateRequest
    ) {
        Comment result = commentWriterService.update(userId, commentId, commentUpdateRequest);
        return ApiResponse.OK(CommentUpdateResponse.from(result));
    }

    @PatchMapping("/comments/{commentId}/status")
    public ApiResponse<CommentDeleteResponse> delete(
        @UserId Long userId,
        @PathVariable(name = "commentId") Long commentId
    ) {
        Comment result = commentWriterService.delete(userId, commentId);
        return ApiResponse.OK(CommentDeleteResponse.from(result));
    }
}
