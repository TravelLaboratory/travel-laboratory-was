package site.travellaboratory.be.controller.article;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.controller.article.dto.ArticleDeleteResponse;
import site.travellaboratory.be.controller.article.dto.ArticleOneResponse;
import site.travellaboratory.be.controller.article.dto.ArticleRegisterRequest;
import site.travellaboratory.be.controller.article.dto.ArticleRegisterResponse;
import site.travellaboratory.be.controller.article.dto.ArticleTotalResponse;
import site.travellaboratory.be.controller.article.dto.ArticleUpdateCoverImageResponse;
import site.travellaboratory.be.controller.article.dto.ArticleUpdateRequest;
import site.travellaboratory.be.controller.article.dto.ArticleUpdateResponse;
import site.travellaboratory.be.service.ArticleService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping("/article")
    public ResponseEntity<ArticleRegisterResponse> registerArticle(
            @RequestBody final ArticleRegisterRequest articleRegisterRequest,
            @UserId final Long userId
    ) {
        final ArticleRegisterResponse articleRegisterResponse = articleService.saveArticle(userId,
                articleRegisterRequest);
        return ResponseEntity.ok(articleRegisterResponse);
    }

    @GetMapping("/articles/{userId}")
    public ResponseEntity<Page<ArticleTotalResponse>> findArticles(
            @UserId final Long loginId,
            @PathVariable(name = "userId") final Long userId,
            @RequestParam(defaultValue = "0", value = "page") int page,
            @RequestParam(defaultValue = "10", value = "size") int size
    ) {
        final Page<ArticleTotalResponse> articleResponse = articleService.findByUserArticles(loginId, userId,
                PageRequest.of(page, size));
        return ResponseEntity.ok(articleResponse);
    }

    @GetMapping("/article/{articleId}")
    public ResponseEntity<ArticleOneResponse> findArticle(
            @UserId final Long loginId,
            @PathVariable(name = "articleId") final Long articleId
    ) {
        final ArticleOneResponse articleResponse = articleService.findByArticle(loginId, articleId);
        return ResponseEntity.ok(articleResponse);
    }

    @PutMapping("/article/{articleId}/coverImage")
    public ResponseEntity<ArticleUpdateCoverImageResponse> updateCoverImage(
            @RequestPart("coverImage") final MultipartFile coverImage,
            @PathVariable(name = "articleId") final Long articleId
    ) {
        final ArticleUpdateCoverImageResponse articleUpdateCoverImageResponse = articleService.updateCoverImage(
                coverImage, articleId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(articleUpdateCoverImageResponse);
    }

    @PutMapping("/article/{articleId}")
    public ResponseEntity<ArticleUpdateResponse> updateArticle(
            @RequestBody final ArticleUpdateRequest articleUpdateRequest,
            @UserId final Long userId,
            @PathVariable(name = "articleId") final Long articleId
    ) {
        final ArticleUpdateResponse articleUpdateResponse = articleService.updateArticle(articleUpdateRequest, userId,
                articleId);
        return ResponseEntity.ok(articleUpdateResponse);
    }

    @GetMapping("/search/article")
    public ResponseEntity<Page<ArticleTotalResponse>> searchArticle(
            @RequestParam("keyword") final String keyword,
            @RequestParam(defaultValue = "0", value = "page") int page,
            @RequestParam(defaultValue = "10", value = "size") int size,
            @RequestParam(required = false, defaultValue = "createdAt,desc") String sort,
            @UserId final Long loginId
    ) {
        final Page<ArticleTotalResponse> response = articleService.searchArticlesByKeyWord(keyword,
                PageRequest.of(page, size), loginId, sort);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/article/status/{articleId}")
    public ResponseEntity<ArticleDeleteResponse> deleteArticle(
            @UserId final Long userId,
            @PathVariable(name = "articleId") final Long articleId
    ) {
        ArticleDeleteResponse articleDeleteResponse = articleService.deleteArticle(userId, articleId);
        return ResponseEntity.ok(articleDeleteResponse);
    }

    @GetMapping("/banner/articles")
    public ResponseEntity<List<ArticleTotalResponse>> getBannerNotUserArticles() {
        List<ArticleTotalResponse> articles = articleService.getBannerNotUserArticles();
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/auth/banner/articles")
    public ResponseEntity<List<ArticleTotalResponse>> getBannerUserArticles(@UserId final Long userId) {
        List<ArticleTotalResponse> articles = articleService.getBannerUserArticles(userId);
        return ResponseEntity.ok(articles);
    }
}
