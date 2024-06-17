package site.travellaboratory.be.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.controller.article.dto.ArticleAuthorityResponse;
import site.travellaboratory.be.controller.article.dto.ArticleDeleteResponse;
import site.travellaboratory.be.controller.article.dto.ArticleRegisterRequest;
import site.travellaboratory.be.controller.article.dto.ArticleResponse;
import site.travellaboratory.be.controller.article.dto.ArticleSearchRequest;
import site.travellaboratory.be.controller.article.dto.ArticleSearchResponse;
import site.travellaboratory.be.controller.review.dto.ReviewDeleteResponse;
import site.travellaboratory.be.domain.article.Article;
import site.travellaboratory.be.domain.article.ArticleRepository;
import site.travellaboratory.be.domain.article.ArticleStatus;
import site.travellaboratory.be.domain.review.Review;
import site.travellaboratory.be.domain.review.ReviewStatus;
import site.travellaboratory.be.domain.user.UserRepository;
import site.travellaboratory.be.domain.user.entity.User;
import site.travellaboratory.be.domain.user.entity.UserStatus;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    //아티클 저장
    @Transactional
    public Long saveArticle(final Long userId, final ArticleRegisterRequest articleRegisterRequest) {
        final User user = userRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND,
                        HttpStatus.NOT_FOUND));

        final Article article = Article.of(user, articleRegisterRequest);
        articleRepository.save(article);
        return article.getId();
    }

    // 내 아티클 전체 조회
    @Transactional
    public List<ArticleResponse> findByUserArticles(final Long userId) {
        final User user = userRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND,
                        HttpStatus.NOT_FOUND));

        final List<Article> articles = articleRepository.findByUserAndStatusIn(user,
                        List.of(ArticleStatus.ACTIVE, ArticleStatus.PRIVATE))
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.ARTICLE_NOT_FOUND, HttpStatus.NOT_FOUND));

        return ArticleResponse.from(articles);
    }

    // 내 아티클 한개 조회
    @Transactional
    public ArticleResponse findByUserArticle(final Long articleId) {
        final Article article = articleRepository.findByIdAndStatusIn(articleId,
                        List.of(ArticleStatus.ACTIVE, ArticleStatus.PRIVATE))
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.ARTICLE_NOT_FOUND, HttpStatus.NOT_FOUND));

        return ArticleResponse.from(article);
    }

    // 아티클 검색
    public List<ArticleSearchResponse> searchArticlesByKeyWord(final ArticleSearchRequest articleSearchRequest) {
        final List<Article> articles = articleRepository.findByLocationContainingAndStatusActive(
                articleSearchRequest.keyWord());
        return ArticleSearchResponse.from(articles);
    }

    // 아티클 공개 범위 전환
    @Transactional
    public ArticleAuthorityResponse changeAuthorityArticle(final Long userId, final Long articleId) {
        final Article article = articleRepository.findByIdAndStatusIn(articleId,
                        List.of(ArticleStatus.ACTIVE, ArticleStatus.PRIVATE))
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.ARTICLE_NOT_FOUND, HttpStatus.NOT_FOUND));

        if (article.getStatus() == ArticleStatus.PRIVATE && (!article.getUser().getId().equals(userId))) {
            throw new BeApplicationException(ErrorCodes.ARTICLE_READ_DETAIL_NOT_AUTHORIZATION,
                    HttpStatus.UNAUTHORIZED);
        }
        article.toggleStatus();
        return ArticleAuthorityResponse.from(article);
    }


    @Transactional
    public ArticleDeleteResponse deleteReview(final Long userId, final Long articleId) {
        final Article article = articleRepository.findByIdAndStatusIn(articleId,
                        List.of(ArticleStatus.ACTIVE, ArticleStatus.PRIVATE))
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.ARTICLE_NOT_FOUND, HttpStatus.NOT_FOUND));

        if (!article.getUser().getId().equals(userId)) {
            throw new BeApplicationException(ErrorCodes.ARTICLE_DELETE_NOT_USER, HttpStatus.FORBIDDEN);
        }

        article.delete();
        articleRepository.save(article);
        return ArticleDeleteResponse.from(true);
    }
}

