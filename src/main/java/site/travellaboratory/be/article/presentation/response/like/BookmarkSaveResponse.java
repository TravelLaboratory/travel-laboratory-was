package site.travellaboratory.be.article.presentation.response.like;

import site.travellaboratory.be.article.infrastructure.persistence.entity.BookmarkEntity;
import site.travellaboratory.be.article.domain.enums.BookmarkStatus;

public record BookmarkSaveResponse (
        BookmarkStatus status
){
    public static BookmarkSaveResponse from(BookmarkEntity bookmarkEntity) {
        return new BookmarkSaveResponse(bookmarkEntity.getStatus());
    }
}
