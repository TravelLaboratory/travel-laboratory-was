package site.travellaboratory.be.controller.review;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.controller.review.dto.ReviewDeleteResponse;
import site.travellaboratory.be.controller.review.dto.ReviewSaveRequest;
import site.travellaboratory.be.controller.review.dto.ReviewSaveResponse;
import site.travellaboratory.be.controller.review.dto.ReviewUpdateRequest;
import site.travellaboratory.be.controller.review.dto.ReviewUpdateResponse;
import site.travellaboratory.be.controller.review.dto.userlikereview.ReviewToggleLikeResponse;
import site.travellaboratory.be.service.ReviewService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/review")
    public ResponseEntity<ReviewSaveResponse> saveReview(
        @UserId Long userId,
        @Valid @RequestBody ReviewSaveRequest reviewSaveRequest
    ) {
        ReviewSaveResponse response = reviewService.saveReview(userId, reviewSaveRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewUpdateResponse> updateReview(
        @UserId Long userId,
        @PathVariable(name = "reviewId") Long reviewId,
        @Valid @RequestBody ReviewUpdateRequest reviewUpdateRequest
    ) {
        ReviewUpdateResponse response = reviewService.updateReview(userId, reviewId, reviewUpdateRequest);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/reviews/{reviewId}/status")
    public ResponseEntity<ReviewDeleteResponse> deleteReview(
        @UserId Long userId,
        @PathVariable(name = "reviewId") Long reviewId
    ) {
        ReviewDeleteResponse response = reviewService.deleteReview(userId, reviewId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/reviews/{reviewId}/likes")
    public ResponseEntity<ReviewToggleLikeResponse> toggleLikeReview(
        @UserId Long userId,
        @PathVariable(name = "reviewId") Long reviewId
    ) {
        ReviewToggleLikeResponse response = reviewService.toggleLikeReview(userId,
            reviewId);
        return ResponseEntity.ok(response);
    }
}
