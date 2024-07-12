package site.travellaboratory.be.presentation.article.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.application.article.ArticleLikeService;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.presentation.article.dto.like.BookmarkSaveResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ArticleLikeController {

    private final ArticleLikeService articleLikeService;

    @PatchMapping("/bookmark/{articleId}")
    public ResponseEntity<BookmarkSaveResponse> registerBookmark(
        @UserId final Long userId,
        @PathVariable(name = "articleId") final Long articleId
    ) {
        final BookmarkSaveResponse bookmarkSaveResponse = articleLikeService.saveBookmark(userId, articleId);
        return ResponseEntity.ok(bookmarkSaveResponse);
    }
}