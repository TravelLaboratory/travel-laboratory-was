package site.travellaboratory.be.comment.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.comment.application.service.CommentLikeService;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.comment.domain.enums.CommentLikeStatus;
import site.travellaboratory.be.comment.presentation.response.like.CommentToggleLikeResponse;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    @PutMapping("/comments/{commentId}/likes")
    public ResponseEntity<CommentToggleLikeResponse> toggleLikeComment(
        @UserId Long userId,
        @PathVariable(name = "commentId") Long commentId
    ) {
        CommentLikeStatus result = commentLikeService.toggleLikeComment(userId, commentId);
        return ResponseEntity.ok(CommentToggleLikeResponse.from(result));
    }
}
