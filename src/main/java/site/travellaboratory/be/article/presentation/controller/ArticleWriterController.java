package site.travellaboratory.be.article.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import site.travellaboratory.be.article.application.service.ArticleWriterService;
import site.travellaboratory.be.article.domain.Article;
import site.travellaboratory.be.article.domain.request.ArticleRegisterRequest;
import site.travellaboratory.be.article.domain.request.ArticleUpdateRequest;
import site.travellaboratory.be.article.presentation.response.writer.ArticleRegisterResponse;
import site.travellaboratory.be.article.presentation.response.writer.ArticleUpdateCoverImageResponse;
import site.travellaboratory.be.article.presentation.response.writer.ArticleUpdatePrivacyResponse;
import site.travellaboratory.be.article.presentation.response.writer.ArticleUpdateResponse;
import site.travellaboratory.be.common.annotation.UserId;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ArticleWriterController {

    private final ArticleWriterService articleWriterService;

    @PostMapping("/article")
    public ResponseEntity<ArticleRegisterResponse> registerArticle(
        @RequestBody ArticleRegisterRequest articleRegisterRequest,
        @UserId Long userId
    ) {
        Long result = articleWriterService.saveArticle(userId, articleRegisterRequest);
        return ResponseEntity.ok(ArticleRegisterResponse.from(result));
    }

    @PutMapping("/article/{articleId}/coverImg")
    public ResponseEntity<ArticleUpdateCoverImageResponse> updateCoverImage(
        @RequestPart("cover_img") final MultipartFile coverImage,
        @PathVariable(name = "articleId") final Long articleId
    ) {
        final ArticleUpdateCoverImageResponse articleUpdateCoverImageResponse = articleWriterService.updateCoverImage(
            coverImage, articleId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(articleUpdateCoverImageResponse);
    }

    @PutMapping("/article/{articleId}")
    public ResponseEntity<ArticleUpdateResponse> updateArticle(
        @RequestBody final ArticleUpdateRequest articleUpdateRequest,
        @UserId final Long userId,
        @PathVariable(name = "articleId") final Long articleId
    ) {
        Article result = articleWriterService.updateArticle(userId, articleId,
            articleUpdateRequest);
        return ResponseEntity.ok(ArticleUpdateResponse.from(result));
    }

    // articleWriterService_에 가는 게 맞는 로직 - 상권 (여행 계획 - 공개, 비공개 설정)
    @PatchMapping("/articles/{articleId}/privacy")
    public ResponseEntity<ArticleUpdatePrivacyResponse> updateArticlePrivacy(
        @UserId Long userId,
        @PathVariable(name = "articleId") Long articleId
    ) {
        ArticleUpdatePrivacyResponse response = articleWriterService.updateArticlePrivacy(
            userId, articleId);
        return ResponseEntity.ok(response);
    }
}
