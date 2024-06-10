package site.travellaboratory.be.controller.dto;

import java.time.LocalDateTime;
import java.util.List;
import site.travellaboratory.be.domain.bookmark.Bookmark;

public record BookmarkResponse(
        String profileImg,
        String title,
        String username,
        LocalDateTime startAt,
        LocalDateTime endAt,
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
