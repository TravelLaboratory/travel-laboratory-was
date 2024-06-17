package site.travellaboratory.be.controller.article;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.controller.article.dto.ArticleAuthorityResponse;
import site.travellaboratory.be.controller.article.dto.ArticleDeleteResponse;
import site.travellaboratory.be.controller.article.dto.ArticleRegisterRequest;
import site.travellaboratory.be.controller.article.dto.ArticleResponse;
import site.travellaboratory.be.controller.article.dto.ArticleSearchRequest;
import site.travellaboratory.be.controller.article.dto.ArticleSearchResponse;
import site.travellaboratory.be.controller.article.dto.ArticleUpdateRequest;
import site.travellaboratory.be.controller.article.dto.ArticleUpdateResponse;
import site.travellaboratory.be.service.ArticleService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping("/register/article")
    public ResponseEntity<Long> registerMyArticle(
            @RequestBody final ArticleRegisterRequest articleRegisterRequest,
            @UserId final Long userId
    ) {
        final Long articleId = articleService.saveArticle(userId, articleRegisterRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(articleId);
    }

    @GetMapping("/find/articles")
    public ResponseEntity<List<ArticleResponse>> findMyArticles(@UserId final Long userId) {
        final List<ArticleResponse> articleResponse = articleService.findByUserArticles(userId);
        return ResponseEntity.ok(articleResponse);
    }

    @GetMapping("/find/article/{articleId}")
    public ResponseEntity<ArticleResponse> findArticle(
            @PathVariable final Long articleId
    ) {
        final ArticleResponse articleResponse = articleService.findByArticle(articleId);
        return ResponseEntity.ok(articleResponse);
    }

    @PutMapping("/update/article/{articleId}")
    public ResponseEntity<ArticleUpdateResponse> updateArticle(
            @RequestBody final ArticleUpdateRequest articleUpdateRequest,
            @UserId final Long userId,
            @PathVariable final Long articleId
    ) {
        final ArticleUpdateResponse articleUpdateResponse = articleService.updateArticle(articleUpdateRequest, userId, articleId);
        return ResponseEntity.ok(articleUpdateResponse);
    }

    @GetMapping("/search/article")
    public ResponseEntity<List<ArticleSearchResponse>> searchArticle(
            @Valid @RequestBody final ArticleSearchRequest articleSearchRequest) {
        final List<ArticleSearchResponse> response = articleService.searchArticlesByKeyWord(articleSearchRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/article/{articleId}/authority")
    public ResponseEntity<ArticleAuthorityResponse> authorityArticle(
            @UserId final Long userId,
            @PathVariable final Long articleId
    ) {
        final ArticleAuthorityResponse articleAuthorityResponse = articleService.changeAuthorityArticle(userId,
                articleId);
        return ResponseEntity.ok(articleAuthorityResponse);
    }

    @PatchMapping("/article/status/{articleId}")
    public ResponseEntity<ArticleDeleteResponse> deleteArticle(
            @UserId final Long userId,
            @PathVariable final Long articleId
    ) {
        ArticleDeleteResponse articleDeleteResponse = articleService.deleteReview(userId, articleId);
        return ResponseEntity.ok(articleDeleteResponse);
    }
}
