package site.travellaboratory.be.review.presentation.controller;

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
import site.travellaboratory.be.review.application.service.ReviewWriterService;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.review.domain.Review;
import site.travellaboratory.be.review.presentation.response.writer.ReviewDeleteResponse;
import site.travellaboratory.be.review.domain.request.ReviewSaveRequest;
import site.travellaboratory.be.review.presentation.response.writer.ReviewSaveResponse;
import site.travellaboratory.be.review.domain.request.ReviewUpdateRequest;
import site.travellaboratory.be.review.presentation.response.writer.ReviewUpdateResponse;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReviewWriterController {

    private final ReviewWriterService reviewWriterService;

    @PostMapping("/review")
    public ResponseEntity<ReviewSaveResponse> save(
        @UserId Long userId,
        @Valid @RequestBody ReviewSaveRequest reviewSaveRequest
    ) {
        Review result = reviewWriterService.save(userId, reviewSaveRequest);

        ReviewSaveResponse response = ReviewSaveResponse.from(result);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewUpdateResponse> update(
        @UserId Long userId,
        @PathVariable(name = "reviewId") Long reviewId,
        @Valid @RequestBody ReviewUpdateRequest reviewUpdateRequest
    ) {
        Review result = reviewWriterService.update(userId, reviewId, reviewUpdateRequest);

        ReviewUpdateResponse response = ReviewUpdateResponse.from(result);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/reviews/{reviewId}/status")
    public ResponseEntity<ReviewDeleteResponse> delete(
        @UserId Long userId,
        @PathVariable(name = "reviewId") Long reviewId
    ) {
        Review result = reviewWriterService.delete(userId, reviewId);
        ReviewDeleteResponse response = ReviewDeleteResponse.from(result);
        return ResponseEntity.ok(response);
    }
}
