package site.travellaboratory.be.review.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.common.presentation.response.ApiResponse;
import site.travellaboratory.be.review.application.service.ReviewReaderService;
import site.travellaboratory.be.review.presentation.response.reader.ProfileReviewPaginationResponse;
import site.travellaboratory.be.review.presentation.response.reader.ReviewReadDetailResponse;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReviewReaderController {

    private final ReviewReaderService reviewReaderService;

    @GetMapping("/reviews/{reviewId}")
    public ApiResponse<ReviewReadDetailResponse> readReviewDetail(
        @UserId Long userId,
        @PathVariable(name = "reviewId") Long reviewId
    ) {
        ReviewReadDetailResponse response = reviewReaderService.readReviewDetail(userId,
            reviewId);
        return ApiResponse.OK(response);
    }

    /* /api/v1/users/{userId}/reviews
     * 프로필 - 후기 전체 조회 [페이지네이션]
     */
    @GetMapping("/users/{userId}/reviews")
    public ApiResponse<ProfileReviewPaginationResponse> readProfileReviews(
        @UserId Long tokenUserId,
        @PathVariable(name = "userId") Long userId,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        ProfileReviewPaginationResponse response = reviewReaderService.readProfileReviews(
            tokenUserId, userId, page, size);
        return ApiResponse.OK(response);
    }
}
