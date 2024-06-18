package site.travellaboratory.be.controller.bookmark.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import site.travellaboratory.be.domain.bookmark.Bookmark;

public record BookmarkResponse(
        String profileImg,
        String title,
        String username,
        LocalDate startAt,
        LocalDate endAt,
        String expense
) {

    public static BookmarkResponse from(Bookmark bookmark) {
        return new BookmarkResponse(bookmark.getUser().getProfileImgUrl(), bookmark.getArticle().getTitle(),
                bookmark.getUser().getUsername(), bookmark.getArticle().getStartAt(), bookmark.getArticle().getEndAt(),
                bookmark.getArticle().getExpense());
    }

    public static List<BookmarkResponse> from(List<Bookmark> bookmarks) {
        return bookmarks.stream()
                .map(BookmarkResponse::from)
                .toList();
    }
}
