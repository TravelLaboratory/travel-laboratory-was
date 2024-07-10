package site.travellaboratory.be.presentation.review;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.application.review.ReviewLikeService;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.presentation.review.dto.userlikereview.ReviewToggleLikeResponse;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReviewLikeController {

    private final ReviewLikeService reviewLikeService;

    @PutMapping("/reviews/{reviewId}/likes")
    public ResponseEntity<ReviewToggleLikeResponse> toggleLikeReview(
        @UserId Long userId,
        @PathVariable(name = "reviewId") Long reviewId
    ) {
        ReviewToggleLikeResponse response = reviewLikeService.toggleLikeReview(userId,
            reviewId);
        return ResponseEntity.ok(response);
    }
}
