package site.travellaboratory.be.controller.bookmark.dto;

import site.travellaboratory.be.domain.bookmark.Bookmark;
import site.travellaboratory.be.domain.bookmark.BookmarkStatus;

public record BookmarkSaveResponse (
        BookmarkStatus status
){
    public static BookmarkSaveResponse from(Bookmark bookmark) {
        return new BookmarkSaveResponse(bookmark.getStatus());
    }
}
