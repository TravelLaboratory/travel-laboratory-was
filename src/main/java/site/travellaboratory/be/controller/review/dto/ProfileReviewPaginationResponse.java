package site.travellaboratory.be.controller.review.dto;

import java.util.List;
import org.springframework.data.domain.Page;
import site.travellaboratory.be.domain.review.Review;

public record ProfileReviewPaginationResponse(
    List<ProfileReviewResponse> reviews,
    int currentPage,
    int pageSize,
    int totalPages,
    long totalComment
) {
    public static ProfileReviewPaginationResponse from(
        List<ProfileReviewResponse> reviews,
        Page<Review> reviewPage
    ) {
        return new ProfileReviewPaginationResponse(
            reviews,
            reviewPage.getNumber(),
            reviewPage.getSize(),
            reviewPage.getTotalPages(),
            reviewPage.getTotalElements()
        );
    }
}
