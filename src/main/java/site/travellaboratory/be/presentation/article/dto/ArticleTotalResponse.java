package site.travellaboratory.be.presentation.article.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import site.travellaboratory.be.infrastructure.domains.article.entity.Article;
import site.travellaboratory.be.infrastructure.domains.article.entity.Location;
import site.travellaboratory.be.infrastructure.domains.article.enums.TravelStyle;

public record ArticleTotalResponse(
        Long articleId,
        String title,
        List<Location> location,
        LocalDate startAt,
        LocalDate endAt,
        String expense,
        String profileImageUrl,
        String coverImageUrl,
        String travelCompanion,
        List<String> travelStyles,
        String name,
        Long bookmarkCount,
        Boolean isBookmarked,
        Boolean isEditable

) {
    public static ArticleTotalResponse of(
            final Article article,
            final Long bookmarkCount,
            final Boolean isBookmarked,
            final Boolean isEditable
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
                article.getCoverImageUrl(),
                article.getTravelCompanion().getName(),
                travelStyleNames,
                article.getUser().getNickname(),
                bookmarkCount,
                isBookmarked,
                isEditable
        );
    }
}
