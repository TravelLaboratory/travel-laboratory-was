package site.travellaboratory.be.controller.bookmark.dto;

import java.time.LocalDate;
import java.util.List;
import site.travellaboratory.be.domain.article.Location;
import site.travellaboratory.be.domain.article.TravelStyle;
import site.travellaboratory.be.domain.bookmark.Bookmark;

public record BookmarkResponse(
        Long articleId,
        String title,
        List<Location> location,
        LocalDate startAt,
        LocalDate endAt,
        String expense,
        String profileImageUrl,
        String coverImage,
        String travelCompanion,
        List<String> travelStyles,
        String name,
        Long bookmarkCount,
        Boolean isBookmarked
) {

    public static BookmarkResponse of(final Bookmark bookmark, final Long bookmarkCount, final Boolean isBookmarked) {
        List<String> travelStyleNames = bookmark.getArticle().getTravelStyles().stream()
                .map(TravelStyle::getName)
                .toList();

        return new BookmarkResponse(
                bookmark.getArticle().getId(),
                bookmark.getArticle().getTitle(),
                bookmark.getArticle().getLocation(),
                bookmark.getArticle().getStartAt(),
                bookmark.getArticle().getEndAt(),
                bookmark.getArticle().getExpense(),
                bookmark.getArticle().getUser().getProfileImgUrl(),
                bookmark.getArticle().getCoverImageUrl(),
                bookmark.getArticle().getTravelCompanion().getName(),
                travelStyleNames,
                bookmark.getArticle().getNickname(),
                bookmarkCount,
                isBookmarked
        );
    }
}
