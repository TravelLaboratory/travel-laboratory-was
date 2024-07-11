package site.travellaboratory.be.presentation.article.dto.like;

import site.travellaboratory.be.infrastructure.domains.bookmark.entity.Bookmark;
import site.travellaboratory.be.infrastructure.domains.bookmark.enums.BookmarkStatus;

public record BookmarkSaveResponse (
        BookmarkStatus status
){
    public static BookmarkSaveResponse from(Bookmark bookmark) {
        return new BookmarkSaveResponse(bookmark.getStatus());
    }
}
