package site.travellaboratory.be.article.presentation.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.article.application.service.ArticleReaderService;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.article.presentation.response.like.BookmarkResponse;
import site.travellaboratory.be.article.presentation.response.reader.ArticleOneResponse;
import site.travellaboratory.be.article.presentation.response.reader.ArticleTotalResponse;
import site.travellaboratory.be.common.presentation.response.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ArticleReaderController {

    private final ArticleReaderService articleReaderService;

    @GetMapping("/articles/{userId}")
    public ResponseEntity<ApiResponse<Page<ArticleTotalResponse>>> findArticlesByUser(
            @UserId final Long loginId,
            @PathVariable(name = "userId") final Long userId,
            @RequestParam(defaultValue = "0", value = "page") int page,
            @RequestParam(defaultValue = "10", value = "size") int size
    ) {
        final Page<ArticleTotalResponse> articleResponse = articleReaderService.findByUserArticles(loginId, userId,
                PageRequest.of(page, size));
        return ResponseEntity.ok(ApiResponse.OK(articleResponse));
    }

    @GetMapping("/articles")
    public ResponseEntity<ApiResponse<Page<ArticleTotalResponse>>> findArticles(
            @UserId final Long loginId,
            @RequestParam(defaultValue = "0", value = "page") int page,
            @RequestParam(defaultValue = "10", value = "size") int size,
            @RequestParam(value = "sort", defaultValue = "createdAt,DESC") String sort
    ) {
        final Page<ArticleTotalResponse> articleTotalResponses = articleReaderService.searchAllArticles(loginId,
                PageRequest.of(page, size), sort);
        return ResponseEntity.ok(ApiResponse.OK(articleTotalResponses));
    }

    @GetMapping("/article/{articleId}")
    public ResponseEntity<ApiResponse<ArticleOneResponse>> findArticle(
            @UserId final Long loginId,
            @PathVariable(name = "articleId") final Long articleId
    ) {
        final ArticleOneResponse articleResponse = articleReaderService.findByArticle(loginId, articleId);
        return ResponseEntity.ok(ApiResponse.OK(articleResponse));
    }


    @GetMapping("/search/article")
    public ResponseEntity<ApiResponse<Page<ArticleTotalResponse>>> searchArticle(
            @RequestParam("keyword") final String keyword,
            @RequestParam(defaultValue = "0", value = "page") int page,
            @RequestParam(defaultValue = "10", value = "size") int size,
            @RequestParam(required = false, defaultValue = "createdAt,desc") String sort,
            @UserId final Long loginId
    ) {
        final Page<ArticleTotalResponse> response = articleReaderService.searchArticlesByKeyWord(keyword,
                PageRequest.of(page, size), loginId, sort);
        return ResponseEntity.ok(ApiResponse.OK(response));
    }

    @GetMapping("/banner/articles")
    public ResponseEntity<ApiResponse<List<ArticleTotalResponse>>> getBannerNotUserArticles() {
        List<ArticleTotalResponse> articles = articleReaderService.getBannerNotUserArticles();
        return ResponseEntity.ok(ApiResponse.OK(articles));
    }

    @GetMapping("/auth/banner/articles")
    public ResponseEntity<ApiResponse<List<ArticleTotalResponse>>> getBannerUserArticles(@UserId final Long userId) {
        List<ArticleTotalResponse> articles = articleReaderService.getBannerUserArticles(userId);
        return ResponseEntity.ok(ApiResponse.OK(articles));
    }

    @GetMapping("/bookmarks/{userId}")
    public ResponseEntity<ApiResponse<Page<BookmarkResponse>>> findMyAllBookmark(
            @UserId final Long loginId,
            @PathVariable(name = "userId") final Long userId,
            @RequestParam(defaultValue = "0", value = "page") int page,
            @RequestParam(defaultValue = "10", value = "size") int size
    ) {
        final Page<BookmarkResponse> allBookmarkByUser = articleReaderService.findAllBookmarkByUser(loginId, userId,
                PageRequest.of(page, size));

        return ResponseEntity.ok(ApiResponse.OK(allBookmarkByUser));
    }
}
