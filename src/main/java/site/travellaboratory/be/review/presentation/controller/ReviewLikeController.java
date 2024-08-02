package site.travellaboratory.be.review.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.common.presentation.response.ApiResponse;
import site.travellaboratory.be.review.application.service.ReviewLikeService;
import site.travellaboratory.be.review.domain.ReviewLike;
import site.travellaboratory.be.review.presentation.response.like.ReviewToggleLikeResponse;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReviewLikeController {

    private final ReviewLikeService reviewLikeService;

    @PutMapping("/reviews/{reviewId}/likes")
    public ResponseEntity<ApiResponse<ReviewToggleLikeResponse>> toggleLike(
        @UserId Long userId,
        @PathVariable(name = "reviewId") Long reviewId
    ) {
        ReviewLike result = reviewLikeService.toggleLike(userId, reviewId);
        return ResponseEntity.ok(ApiResponse.OK(ReviewToggleLikeResponse.from(result)));
    }
}
