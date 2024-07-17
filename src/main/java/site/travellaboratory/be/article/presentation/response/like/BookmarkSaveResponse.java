package site.travellaboratory.be.article.presentation.response.like;

import site.travellaboratory.be.article.infrastructure.persistence.entity.Bookmark;
import site.travellaboratory.be.article.domain.enums.BookmarkStatus;

public record BookmarkSaveResponse (
        BookmarkStatus status
){
    public static BookmarkSaveResponse from(Bookmark bookmark) {
        return new BookmarkSaveResponse(bookmark.getStatus());
    }
}
