package site.travellaboratory.be.presentation.article.controller;

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
import site.travellaboratory.be.application.article.ArticleWriterService;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.presentation.article.dto.writer.ArticleDeleteResponse;
import site.travellaboratory.be.presentation.article.dto.writer.ArticleRegisterRequest;
import site.travellaboratory.be.presentation.article.dto.writer.ArticleRegisterResponse;
import site.travellaboratory.be.presentation.article.dto.writer.ArticleUpdateCoverImageResponse;
import site.travellaboratory.be.presentation.article.dto.writer.ArticleUpdatePrivacyResponse;
import site.travellaboratory.be.presentation.article.dto.writer.ArticleUpdateRequest;
import site.travellaboratory.be.presentation.article.dto.writer.ArticleUpdateResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ArticleWriterController {

    private final ArticleWriterService articleWriterService;

    @PostMapping("/article")
    public ResponseEntity<ArticleRegisterResponse> registerArticle(
        @RequestBody final ArticleRegisterRequest articleRegisterRequest,
        @UserId final Long userId
    ) {
        final ArticleRegisterResponse articleRegisterResponse = articleWriterService.saveArticle(userId,
            articleRegisterRequest);
        return ResponseEntity.ok(articleRegisterResponse);
    }

    @PutMapping("/article/{articleId}/coverImage")
    public ResponseEntity<ArticleUpdateCoverImageResponse> updateCoverImage(
        @RequestPart("coverImage") final MultipartFile coverImage,
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
        final ArticleUpdateResponse articleUpdateResponse = articleWriterService.updateArticle(articleUpdateRequest, userId,
            articleId);
        return ResponseEntity.ok(articleUpdateResponse);
    }

    @PatchMapping("/article/status/{articleId}")
    public ResponseEntity<ArticleDeleteResponse> deleteArticle(
        @UserId final Long userId,
        @PathVariable(name = "articleId") final Long articleId
    ) {
        ArticleDeleteResponse articleDeleteResponse = articleWriterService.deleteArticle(userId, articleId);
        return ResponseEntity.ok(articleDeleteResponse);
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