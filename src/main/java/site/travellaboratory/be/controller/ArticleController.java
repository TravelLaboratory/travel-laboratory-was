package site.travellaboratory.be.controller;


import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.controller.dto.article.ArticleRegisterRequest;
import site.travellaboratory.be.controller.dto.article.ArticleResponse;
import site.travellaboratory.be.service.ArticleService;

@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping("/user/{userId}/article")
    public ResponseEntity<Long> registerArticle(
            @RequestBody final ArticleRegisterRequest articleRegisterRequest,
            @PathVariable final Long userId
    ) {
        final Long articleId = articleService.saveArticle(userId, articleRegisterRequest);
        return ResponseEntity.created(URI.create("/user/article/" + articleId)).build();
    }

    @GetMapping("/user/{userId}/articles")
    public ResponseEntity<List<ArticleResponse>> findArticles(@PathVariable final Long userId) {
        final List<ArticleResponse> articleResponse = articleService.findByUserArticles(userId);
        return ResponseEntity.ok(articleResponse);
    }

    @GetMapping("/user/articles/{articleId}")
    public ResponseEntity<ArticleResponse> findArticle(
            @PathVariable final Long articleId
    ) {
        final ArticleResponse articleResponse = articleService.findByUserArticle(articleId);
        return ResponseEntity.ok(articleResponse);
    }
}
