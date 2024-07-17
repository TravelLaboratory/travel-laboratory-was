package site.travellaboratory.be.application.article;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.infrastructure.domains.article.ArticleJpaRepository;
import site.travellaboratory.be.infrastructure.domains.article.entity.ArticleEntity;
import site.travellaboratory.be.domain.article.enums.ArticleStatus;
import site.travellaboratory.be.infrastructure.domains.bookmark.BookmarkRepository;
import site.travellaboratory.be.infrastructure.domains.bookmark.entity.Bookmark;
import site.travellaboratory.be.infrastructure.domains.user.UserJpaRepository;
import site.travellaboratory.be.infrastructure.domains.user.entity.UserEntity;
import site.travellaboratory.be.domain.user.enums.UserStatus;
import site.travellaboratory.be.presentation.article.dto.like.BookmarkSaveResponse;

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

        final ArticleEntity articleEntity = articleJpaRepository.findByIdAndStatusIn(articleId,
                        List.of(ArticleStatus.ACTIVE, ArticleStatus.PRIVATE))
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.ARTICLE_NOT_FOUND, HttpStatus.NOT_FOUND));

        Bookmark bookmark = bookmarkRepository.findByArticleEntityAndUserEntity(articleEntity,
            userEntity).orElse(null);

        if (bookmark != null) {
            bookmark.toggleStatus();
        } else {
            bookmark = Bookmark.of(userEntity, articleEntity);
        }

        final Bookmark newBookmark = bookmarkRepository.save(bookmark);
        return BookmarkSaveResponse.from(newBookmark);
    }
}

