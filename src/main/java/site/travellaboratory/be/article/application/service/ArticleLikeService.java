package site.travellaboratory.be.article.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.article.domain.enums.ArticleStatus;
import site.travellaboratory.be.article.infrastructure.persistence.entity.ArticleEntity;
import site.travellaboratory.be.article.infrastructure.persistence.entity.BookmarkEntity;
import site.travellaboratory.be.article.infrastructure.persistence.repository.ArticleJpaRepository;
import site.travellaboratory.be.article.infrastructure.persistence.repository.BookmarkRepository;
import site.travellaboratory.be.article.presentation.response.like.BookmarkSaveResponse;
import site.travellaboratory.be.common.presentation.error.ErrorCodes;
import site.travellaboratory.be.common.presentation.exception.BeApplicationException;
import site.travellaboratory.be.user.domain.enums.UserStatus;
import site.travellaboratory.be.user.infrastructure.persistence.entity.UserEntity;
import site.travellaboratory.be.user.infrastructure.persistence.repository.UserJpaRepository;

@Service
@RequiredArgsConstructor
public class ArticleLikeService {

    private final ArticleJpaRepository articleJpaRepository;
    private final BookmarkRepository bookmarkRepository;
    private final UserJpaRepository userJpaRepository;

    @Transactional
    public BookmarkSaveResponse saveBookmark(final Long userId, final Long articleId) {
        final UserEntity userEntity = userJpaRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND,
                        HttpStatus.NOT_FOUND));

        final ArticleEntity articleEntity = articleJpaRepository.findByIdAndStatus(articleId, ArticleStatus.ACTIVE)
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.ARTICLE_NOT_FOUND, HttpStatus.NOT_FOUND));

        BookmarkEntity bookmarkEntity = bookmarkRepository.findByArticleEntityAndUserEntity(articleEntity,
            userEntity).orElse(null);

        if (bookmarkEntity != null) {
            bookmarkEntity.toggleStatus();
        } else {
            bookmarkEntity = BookmarkEntity.of(userEntity, articleEntity);
        }

        final BookmarkEntity newBookmarkEntity = bookmarkRepository.save(bookmarkEntity);
        return BookmarkSaveResponse.from(newBookmarkEntity);
    }
}

