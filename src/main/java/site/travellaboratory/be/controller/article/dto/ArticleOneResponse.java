package site.travellaboratory.be.controller.article.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import site.travellaboratory.be.domain.article.Article;
import site.travellaboratory.be.domain.article.Location;
import site.travellaboratory.be.domain.article.TravelStyle;

public record ArticleOneResponse(
        String title,
        List<Location> location,
        LocalDate startAt,
        LocalDate endAt,
        String expense,
        String coverImage,
        String travelCompanion,
        List<String> travelStyles,
        String name,
        Long bookmarkCount,
        Boolean isBookmarked,
        Boolean isPrivate,
        Boolean isEditable
) {

    public static ArticleOneResponse of(
            final Article article,
            final Long bookmarkCount,
            final Boolean isBookmarked,
            final Boolean isPrivate,
            final Boolean isEditable
    ) {
        List<String> travelStyleNames = article.getTravelStyles().stream()
                .map(TravelStyle::getName)
                .collect(Collectors.toList());

        return new ArticleOneResponse(
                article.getTitle(),
                article.getLocation(),
                article.getStartAt(),
                article.getEndAt(),
                article.getExpense(),
                article.getCoverImageUrl(),
                article.getTravelCompanion().getName(),
                travelStyleNames,
                article.getUser().getNickname(),
                bookmarkCount,
                isBookmarked,
                isPrivate,
                isEditable
        );
    }
}
