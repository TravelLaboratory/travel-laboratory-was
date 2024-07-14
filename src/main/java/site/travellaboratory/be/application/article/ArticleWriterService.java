package site.travellaboratory.be.application.article;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.domain.article.Article;
import site.travellaboratory.be.domain.article.enums.ArticleStatus;
import site.travellaboratory.be.domain.article.enums.TravelCompanion;
import site.travellaboratory.be.domain.article.enums.TravelStyle;
import site.travellaboratory.be.domain.user.enums.UserStatus;
import site.travellaboratory.be.domain.user.user.User;
import site.travellaboratory.be.infrastructure.aws.S3FileUploader;
import site.travellaboratory.be.infrastructure.domains.article.ArticleJpaRepository;
import site.travellaboratory.be.infrastructure.domains.article.entity.ArticleJpaEntity;
import site.travellaboratory.be.infrastructure.domains.user.UserJpaRepository;
import site.travellaboratory.be.presentation.article.dto.writer.ArticleDeleteResponse;
import site.travellaboratory.be.presentation.article.dto.writer.ArticleRegisterRequest;
import site.travellaboratory.be.presentation.article.dto.writer.ArticleUpdateCoverImageResponse;
import site.travellaboratory.be.presentation.article.dto.writer.ArticleUpdatePrivacyResponse;
import site.travellaboratory.be.presentation.article.dto.writer.ArticleUpdateRequest;
import site.travellaboratory.be.presentation.article.dto.writer.LocationDto;

@Service
@RequiredArgsConstructor
public class ArticleWriterService {

    private final ArticleJpaRepository articleJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final S3FileUploader s3FileUploader;

    //내 초기 여행 계획 저장
    @Transactional
    public Long saveArticle(final Long userId, final ArticleRegisterRequest request) {
        User user = userJpaRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND,
                        HttpStatus.NOT_FOUND)).toModel();

        Article article = Article.create(user,
            request.title(),
            request.locations().stream().map(LocationDto::toModel).toList(),
            request.startAt(),
            request.endAt(),
            request.expense(),
            TravelCompanion.from(request.travelCompanion()),
            TravelStyle.from(request.travelStyles()));

        ArticleJpaEntity savedArticle = articleJpaRepository.save(ArticleJpaEntity.from(article));
        return savedArticle.getId();
    }

    @Transactional
    public ArticleUpdateCoverImageResponse updateCoverImage(
            final MultipartFile coverImage,
            final Long articleId) {
        final ArticleJpaEntity articleJpaEntity = articleJpaRepository.findByIdAndStatusIn(articleId,
                        List.of(ArticleStatus.ACTIVE, ArticleStatus.PRIVATE))
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.ARTICLE_NOT_FOUND, HttpStatus.NOT_FOUND));

        final String url = s3FileUploader.uploadFiles(coverImage);

        articleJpaEntity.updateCoverImage(url);

        return new ArticleUpdateCoverImageResponse(url);
    }

    // 내 초기 여행 계획 수정
    @Transactional
    public Article updateArticle(Long userId, Long articleId, ArticleUpdateRequest request) {
        Article article = articleJpaRepository.findByIdAndStatusIn(articleId,
                        List.of(ArticleStatus.ACTIVE, ArticleStatus.PRIVATE))
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.ARTICLE_NOT_FOUND, HttpStatus.NOT_FOUND)).toModel();


        User user = userJpaRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND,
                HttpStatus.NOT_FOUND)).toModel();

        // todo: Article로 이동
        if (!article.getUser().getId().equals(user.getId())) {
            throw new BeApplicationException(ErrorCodes.ARTICLE_UPDATE_NOT_USER, HttpStatus.UNAUTHORIZED);
        }

        Article updateArticle = article.update(user,
            request.title(),
            request.locations().stream().map(LocationDto::toModel).toList(),
            request.startAt(),
            request.endAt(),
            request.expense(),
            TravelCompanion.from(request.travelCompanion()),
            TravelStyle.from(request.travelStyles()));

        return articleJpaRepository.save(ArticleJpaEntity.from(updateArticle)).toModel();
    }

    // 아티클 삭제
    @Transactional
    public ArticleDeleteResponse deleteArticle(final Long userId, final Long articleId) {
        final ArticleJpaEntity articleJpaEntity = articleJpaRepository.findByIdAndStatusIn(articleId,
                        List.of(ArticleStatus.ACTIVE, ArticleStatus.PRIVATE))
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.ARTICLE_NOT_FOUND, HttpStatus.NOT_FOUND));

        if (!articleJpaEntity.getUserJpaEntity().getId().equals(userId)) {
            throw new BeApplicationException(ErrorCodes.ARTICLE_DELETE_NOT_USER, HttpStatus.FORBIDDEN);
        }

        articleJpaEntity.delete();
        articleJpaRepository.save(articleJpaEntity);
        return ArticleDeleteResponse.from(true);
    }

    // articleService_에 가는 게 맞는 로직
    @Transactional
    public ArticleUpdatePrivacyResponse updateArticlePrivacy(Long userId, Long articleId) {
        // 유효하지 않은 초기 여행 계획(article_id) 의 수정(공개, 비공개)하려고 할 경우
        ArticleJpaEntity articleJpaEntity = articleJpaRepository.findByIdAndStatusIn(articleId, List.of(
                ArticleStatus.ACTIVE, ArticleStatus.PRIVATE))
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.ARTICLE_SCHEDULE_PRIVACY_INVALID,
                    HttpStatus.NOT_FOUND));

        // 유저가 작성한 초기 여행 계획(article_id)이 아닌 경우
        if (!articleJpaEntity.getUserJpaEntity().getId().equals(userId)) {
            throw new BeApplicationException(ErrorCodes.ARTICLE_SCHEDULE_PRIVACY_NOT_USER,
                HttpStatus.FORBIDDEN);
        }

        // 초기 여행 계획 비공개 여부 수정
        articleJpaEntity.togglePrivacyStatus();

        // 비공개 true, 공개 false
        boolean isPrivate = (articleJpaEntity.getStatus() == ArticleStatus.PRIVATE);

        return ArticleUpdatePrivacyResponse.from(isPrivate);
    }
}

