package site.travellaboratory.be.application.article;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.infrastructure.aws.S3FileUploader;
import site.travellaboratory.be.infrastructure.domains.article.ArticleRepository;
import site.travellaboratory.be.infrastructure.domains.article.entity.Article;
import site.travellaboratory.be.infrastructure.domains.article.enums.ArticleStatus;
import site.travellaboratory.be.infrastructure.domains.user.UserRepository;
import site.travellaboratory.be.infrastructure.domains.user.entity.UserJpaEntity;
import site.travellaboratory.be.domain.user.enums.UserStatus;
import site.travellaboratory.be.presentation.article.dto.writer.ArticleDeleteResponse;
import site.travellaboratory.be.presentation.article.dto.writer.ArticleRegisterRequest;
import site.travellaboratory.be.presentation.article.dto.writer.ArticleRegisterResponse;
import site.travellaboratory.be.presentation.article.dto.writer.ArticleUpdateCoverImageResponse;
import site.travellaboratory.be.presentation.article.dto.writer.ArticleUpdatePrivacyResponse;
import site.travellaboratory.be.presentation.article.dto.writer.ArticleUpdateRequest;
import site.travellaboratory.be.presentation.article.dto.writer.ArticleUpdateResponse;

@Service
@RequiredArgsConstructor
public class ArticleWriterService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final S3FileUploader s3FileUploader;

    //내 초기 여행 계획 저장
    @Transactional
    public ArticleRegisterResponse saveArticle(final Long userId, final ArticleRegisterRequest articleRegisterRequest) {
        final UserJpaEntity userJpaEntity = userRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND,
                        HttpStatus.NOT_FOUND));

        final Article article = Article.of(userJpaEntity, articleRegisterRequest);
        articleRepository.save(article);
        return ArticleRegisterResponse.from(article.getId());
    }

    @Transactional
    public ArticleUpdateCoverImageResponse updateCoverImage(
            final MultipartFile coverImage,
            final Long articleId) {
        final Article article = articleRepository.findByIdAndStatusIn(articleId,
                        List.of(ArticleStatus.ACTIVE, ArticleStatus.PRIVATE))
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.ARTICLE_NOT_FOUND, HttpStatus.NOT_FOUND));

        final String url = s3FileUploader.uploadFiles(coverImage);

        article.updateCoverImage(url);

        return new ArticleUpdateCoverImageResponse(url);
    }

    // 내 초기 여행 계획 수정
    @Transactional
    public ArticleUpdateResponse updateArticle(
            final ArticleUpdateRequest articleUpdateRequest,
            final Long userId,
            final Long articleId
    ) {
        final Article article = articleRepository.findByIdAndStatusIn(articleId,
                        List.of(ArticleStatus.ACTIVE, ArticleStatus.PRIVATE))
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.ARTICLE_NOT_FOUND, HttpStatus.NOT_FOUND));

        if (!article.getUserJpaEntity().getId().equals(userId)) {
            throw new BeApplicationException(ErrorCodes.ARTICLE_UPDATE_NOT_USER, HttpStatus.UNAUTHORIZED);
        }

        article.update(articleUpdateRequest);
        return ArticleUpdateResponse.from(article);
    }

    // 아티클 삭제
    @Transactional
    public ArticleDeleteResponse deleteArticle(final Long userId, final Long articleId) {
        final Article article = articleRepository.findByIdAndStatusIn(articleId,
                        List.of(ArticleStatus.ACTIVE, ArticleStatus.PRIVATE))
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.ARTICLE_NOT_FOUND, HttpStatus.NOT_FOUND));

        if (!article.getUserJpaEntity().getId().equals(userId)) {
            throw new BeApplicationException(ErrorCodes.ARTICLE_DELETE_NOT_USER, HttpStatus.FORBIDDEN);
        }

        article.delete();
        articleRepository.save(article);
        return ArticleDeleteResponse.from(true);
    }

    // articleService_에 가는 게 맞는 로직
    @Transactional
    public ArticleUpdatePrivacyResponse updateArticlePrivacy(Long userId, Long articleId) {
        // 유효하지 않은 초기 여행 계획(article_id) 의 수정(공개, 비공개)하려고 할 경우
        Article article = articleRepository.findByIdAndStatusIn(articleId, List.of(
                ArticleStatus.ACTIVE, ArticleStatus.PRIVATE))
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.ARTICLE_SCHEDULE_PRIVACY_INVALID,
                    HttpStatus.NOT_FOUND));

        // 유저가 작성한 초기 여행 계획(article_id)이 아닌 경우
        if (!article.getUserJpaEntity().getId().equals(userId)) {
            throw new BeApplicationException(ErrorCodes.ARTICLE_SCHEDULE_PRIVACY_NOT_USER,
                HttpStatus.FORBIDDEN);
        }

        // 초기 여행 계획 비공개 여부 수정
        article.togglePrivacyStatus();

        // 비공개 true, 공개 false
        boolean isPrivate = (article.getStatus() == ArticleStatus.PRIVATE);

        return ArticleUpdatePrivacyResponse.from(isPrivate);
    }
}

