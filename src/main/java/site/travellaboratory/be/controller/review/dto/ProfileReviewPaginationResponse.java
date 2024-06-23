package site.travellaboratory.be.controller.review.dto;

import java.util.List;
import org.springframework.data.domain.Page;

public record ProfileReviewPaginationResponse(
    List<ProfileReviewResponse> reviews,
    int currentPage,
    int pageSize,
    int totalPages,
    long totalComment
) {
    public static ProfileReviewPaginationResponse from(
        List<ProfileReviewResponse> reviews,
        Page<Long> reviewPage
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
