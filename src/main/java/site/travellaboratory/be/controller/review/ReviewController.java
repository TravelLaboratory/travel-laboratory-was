package site.travellaboratory.be.controller.review;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.controller.review.dto.ProfileReviewPaginationResponse;
import site.travellaboratory.be.controller.review.dto.ReviewDeleteResponse;
import site.travellaboratory.be.controller.review.dto.ReviewReadDetailResponse;
import site.travellaboratory.be.controller.review.dto.ReviewSaveRequest;
import site.travellaboratory.be.controller.review.dto.ReviewSaveResponse;
import site.travellaboratory.be.controller.review.dto.ReviewUpdateRequest;
import site.travellaboratory.be.controller.review.dto.ReviewUpdateResponse;
import site.travellaboratory.be.controller.review.dto.home.ReviewBannerListResponse;
import site.travellaboratory.be.controller.review.dto.userlikereview.ReviewToggleLikeResponse;
import site.travellaboratory.be.service.ReviewService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewReadDetailResponse> readReviewDetail(
        @UserId Long userId,
        @PathVariable(name = "reviewId") Long reviewId
    ) {
        ReviewReadDetailResponse response = reviewService.readReviewDetail(userId,
            reviewId);
        return ResponseEntity.ok(response);
    }

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
        ReviewUpdateResponse response = reviewService.updateReview(userId, reviewId,
            reviewUpdateRequest);
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


    /* /api/v1/users/{userId}/reviews
     * 프로필 - 후기 전체 조회 [페이지네이션]
     */
    @GetMapping("/users/{userId}/reviews")
    public ResponseEntity<ProfileReviewPaginationResponse> ReadProfileReviews(
        @UserId Long tokenUserId,
        @PathVariable(name = "userId") Long userId,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "int", defaultValue = "10") int size
    ) {
        ProfileReviewPaginationResponse response = reviewService.readProfileReviews(
            tokenUserId, userId, page, size);
        return ResponseEntity.ok(response);
    }

    /*
    * GET
    * /api/v1/banner/reviews
    * 홈(배너) 최신 여행 후기 - 조회 리스트 8개 [feat. 비회원, 회원 공통 항상]
    * */
    @GetMapping("/banner/reviews")
    public ResponseEntity<ReviewBannerListResponse> readBannerReviews() {
        ReviewBannerListResponse response = reviewService.readBannerReviews();
        return ResponseEntity.ok(response);
    }
}
