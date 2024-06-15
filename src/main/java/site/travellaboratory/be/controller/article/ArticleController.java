package site.travellaboratory.be.controller.article;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.controller.article.dto.ArticleRegisterRequest;
import site.travellaboratory.be.controller.article.dto.ArticleResponse;
import site.travellaboratory.be.controller.article.dto.ArticleSearchRequest;
import site.travellaboratory.be.controller.article.dto.ArticleSearchResponse;
import site.travellaboratory.be.service.ArticleService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping("/article")
    public ResponseEntity<Long> registerMyArticle(
            @RequestBody final ArticleRegisterRequest articleRegisterRequest,
            @UserId final Long userId
    ) {
        final Long articleId = articleService.saveArticle(userId, articleRegisterRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(articleId);
    }

    @GetMapping("/articles")
    public ResponseEntity<List<ArticleResponse>> findMyArticles(@UserId final Long userId) {
        final List<ArticleResponse> articleResponse = articleService.findByUserArticles(userId);
        return ResponseEntity.ok(articleResponse);
    }

    @GetMapping("/articles/{articleId}")
    public ResponseEntity<ArticleResponse> findArticle(
            @PathVariable final Long articleId
    ) {
        final ArticleResponse articleResponse = articleService.findByUserArticle(articleId);
        return ResponseEntity.ok(articleResponse);
    }

    @GetMapping("/article/search")
    public ResponseEntity<List<ArticleSearchResponse>> searchArticle(
            @Valid @RequestBody final ArticleSearchRequest articleSearchRequest) {
        final List<ArticleSearchResponse> response = articleService.searchArticlesByKeyWord(articleSearchRequest);
        return ResponseEntity.ok(response);
    }
}
