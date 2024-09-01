package site.travellaboratory.be.article.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.travellaboratory.be.article.domain.Article;
import site.travellaboratory.be.article.domain.enums.ArticleStatus;
import site.travellaboratory.be.article.domain.request.ArticleRegisterRequest;
import site.travellaboratory.be.article.domain.request.ArticleUpdateRequest;
import site.travellaboratory.be.article.infrastructure.persistence.entity.ArticleEntity;
import site.travellaboratory.be.article.infrastructure.persistence.repository.ArticleJpaRepository;
import site.travellaboratory.be.article.presentation.response.writer.ArticleUpdateCoverImageResponse;
import site.travellaboratory.be.common.application.ImageUploadService;
import site.travellaboratory.be.common.error.ErrorCodes;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain.enums.UserStatus;
import site.travellaboratory.be.user.infrastructure.persistence.repository.UserJpaRepository;

@Service
@RequiredArgsConstructor
public class ArticleWriterService {

    private final ArticleJpaRepository articleJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final ImageUploadService imageUploadService;

    //내 초기 여행 계획 저장
    @Transactional
    public Long saveArticle(Long userId, ArticleRegisterRequest articleRegisterRequest) {
        User user = getUserById(userId);
        Article article = Article.create(user, articleRegisterRequest);
        ArticleEntity savedArticle = articleJpaRepository.save(ArticleEntity.from(article));
        return savedArticle.getId();
    }

    @Transactional
    public ArticleUpdateCoverImageResponse updateCoverImage(final MultipartFile coverImage, final Long articleId) {
        final ArticleEntity articleEntity = articleJpaRepository.findByIdAndStatus(articleId, ArticleStatus.ACTIVE)
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.ARTICLE_NOT_FOUND, HttpStatus.NOT_FOUND));

        String uploadImgUrl = imageUploadService.uploadCoverImage(coverImage);

        articleEntity.updateCoverImage(uploadImgUrl);
        return new ArticleUpdateCoverImageResponse(uploadImgUrl);
    }

    // 내 초기 여행 계획 수정
    @Transactional
    public Article updateArticle(Long userId, Long articleId, ArticleUpdateRequest articleUpdateRequest) {
        User user = getUserById(userId);

        Article article = getArticleById(articleId);

        article = article.update(user, articleUpdateRequest);
        return articleJpaRepository.save(ArticleEntity.from(article)).toModel();
    }


    private User getUserById(Long userId) {
        return userJpaRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND,
                HttpStatus.NOT_FOUND)).toModel();
    }

    private Article getArticleById(Long articleId) {
        return articleJpaRepository.findByIdAndStatus(articleId, ArticleStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.ARTICLE_NOT_FOUND,
                HttpStatus.NOT_FOUND)).toModel();
    }
}

