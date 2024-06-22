package site.travellaboratory.be.controller.article.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import site.travellaboratory.be.domain.article.Article;
import site.travellaboratory.be.domain.article.Location;
import site.travellaboratory.be.domain.article.TravelStyle;

public record ArticleTotalResponse(
        Long articleId,
        String title,
        List<Location> location,
        LocalDate startAt,
        LocalDate endAt,
        String expense,
        String profileImageUrl,
        String articleImage,
        String travelCompanion,
        List<String> travelStyles,
        String name,
        Long bookmarkCount,
        Boolean isBookmarked,
        Boolean isEditable
//        int totalPages,
//        Long totalElements,
//        int pageNumber,
//        Boolean last
) {
    public static ArticleTotalResponse of(
            final Article article,
            final Long bookmarkCount,
            final Boolean isBookmarked,
            final Boolean isEditable
//            final int totalPages,
//            final long totalElements,
//            final int pageNumber,
//            final Boolean last
    ) {
        List<String> travelStyleNames = article.getTravelStyles().stream()
                .map(TravelStyle::getName)
                .collect(Collectors.toList());

        return new ArticleTotalResponse(
                article.getId(),
                article.getTitle(),
                article.getLocation(),
                article.getStartAt(),
                article.getEndAt(),
                article.getExpense(),
                article.getUser().getProfileImgUrl(),
                article.getImageUrl(),
                article.getTravelCompanion().getName(),
                travelStyleNames,
                article.getUser().getNickname(),
                bookmarkCount,
                isBookmarked,
                isEditable
//                totalPages,
//                totalElements,
//                pageNumber,
//                last
        );
    }
}
