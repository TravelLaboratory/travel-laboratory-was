package site.travellaboratory.be.article.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.article.application.service.ArticleLikeService;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.article.presentation.response.like.BookmarkSaveResponse;

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
