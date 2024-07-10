package site.travellaboratory.be.presentation.review;

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
import site.travellaboratory.be.application.review.ReviewWriterService;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.presentation.review.dto.ReviewDeleteResponse;
import site.travellaboratory.be.presentation.review.dto.ReviewSaveRequest;
import site.travellaboratory.be.presentation.review.dto.ReviewSaveResponse;
import site.travellaboratory.be.presentation.review.dto.ReviewUpdateRequest;
import site.travellaboratory.be.presentation.review.dto.ReviewUpdateResponse;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReviewWriterController {

    private final ReviewWriterService reviewWriterService;

    @PostMapping("/review")
    public ResponseEntity<ReviewSaveResponse> saveReview(
        @UserId Long userId,
        @Valid @RequestBody ReviewSaveRequest reviewSaveRequest
    ) {
        ReviewSaveResponse response = reviewWriterService.saveReview(userId, reviewSaveRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewUpdateResponse> updateReview(
        @UserId Long userId,
        @PathVariable(name = "reviewId") Long reviewId,
        @Valid @RequestBody ReviewUpdateRequest reviewUpdateRequest
    ) {
        ReviewUpdateResponse response = reviewWriterService.updateReview(userId, reviewId,
            reviewUpdateRequest);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/reviews/{reviewId}/status")
    public ResponseEntity<ReviewDeleteResponse> deleteReview(
        @UserId Long userId,
        @PathVariable(name = "reviewId") Long reviewId
    ) {
        ReviewDeleteResponse response = reviewWriterService.deleteReview(userId, reviewId);
        return ResponseEntity.ok(response);
    }
}
