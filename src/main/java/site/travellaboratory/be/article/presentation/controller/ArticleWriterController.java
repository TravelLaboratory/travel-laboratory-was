package site.travellaboratory.be.article.presentation.controller;

import lombok.RequiredArgsConstructor;
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
import site.travellaboratory.be.article.presentation.response.writer.ArticleUpdateResponse;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.common.presentation.response.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ArticleWriterController {

    private final ArticleWriterService articleWriterService;

    @PostMapping("/article")
    public ApiResponse<ArticleRegisterResponse> registerArticle(
        @RequestBody ArticleRegisterRequest articleRegisterRequest,
        @UserId Long userId
    ) {
        Long result = articleWriterService.saveArticle(userId, articleRegisterRequest);
        return ApiResponse.OK(ArticleRegisterResponse.from(result));
    }

    @PutMapping("/article/{articleId}/coverImg")
    public ApiResponse<ArticleUpdateCoverImageResponse> updateCoverImage(
        @RequestPart("cover_img") final MultipartFile coverImage,
        @PathVariable(name = "articleId") final Long articleId
    ) {
        final ArticleUpdateCoverImageResponse articleUpdateCoverImageResponse = articleWriterService.updateCoverImage(
            coverImage, articleId);
        return ApiResponse.OK(articleUpdateCoverImageResponse);
    }

    @PutMapping("/article/{articleId}")
    public ApiResponse<ArticleUpdateResponse> updateArticle(
        @RequestBody final ArticleUpdateRequest articleUpdateRequest,
        @UserId final Long userId,
        @PathVariable(name = "articleId") final Long articleId
    ) {
        Article result = articleWriterService.updateArticle(userId, articleId,
            articleUpdateRequest);
        return ApiResponse.OK(ArticleUpdateResponse.from(result));
    }
}
