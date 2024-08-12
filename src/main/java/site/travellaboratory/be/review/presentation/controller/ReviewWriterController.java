package site.travellaboratory.be.review.presentation.controller;

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
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.common.presentation.response.ApiResponse;
import site.travellaboratory.be.review.application.service.ReviewWriterService;
import site.travellaboratory.be.review.domain.Review;
import site.travellaboratory.be.review.domain.request.ReviewSaveRequest;
import site.travellaboratory.be.review.domain.request.ReviewUpdateRequest;
import site.travellaboratory.be.review.presentation.response.writer.ReviewDeleteResponse;
import site.travellaboratory.be.review.presentation.response.writer.ReviewSaveResponse;
import site.travellaboratory.be.review.presentation.response.writer.ReviewUpdateResponse;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReviewWriterController {

    private final ReviewWriterService reviewWriterService;

    @PostMapping("/review")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ReviewSaveResponse> save(
        @UserId Long userId,
        @Valid @RequestBody ReviewSaveRequest reviewSaveRequest
    ) {
        Review result = reviewWriterService.save(userId, reviewSaveRequest);

        ReviewSaveResponse response = ReviewSaveResponse.from(result);
        return ApiResponse.OK(response);
    }

    @PatchMapping("/reviews/{reviewId}")
    public ApiResponse<ReviewUpdateResponse> update(
        @UserId Long userId,
        @PathVariable(name = "reviewId") Long reviewId,
        @Valid @RequestBody ReviewUpdateRequest reviewUpdateRequest
    ) {
        Review result = reviewWriterService.update(userId, reviewId, reviewUpdateRequest);

        ReviewUpdateResponse response = ReviewUpdateResponse.from(result);
        return ApiResponse.OK(response);
    }

    @PatchMapping("/reviews/{reviewId}/status")
    public ApiResponse<ReviewDeleteResponse> delete(
        @UserId Long userId,
        @PathVariable(name = "reviewId") Long reviewId
    ) {
        Review result = reviewWriterService.delete(userId, reviewId);
        ReviewDeleteResponse response = ReviewDeleteResponse.from(result);
        return ApiResponse.OK(response);
    }
}
