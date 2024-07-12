package site.travellaboratory.be.application.auth;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.infrastructure.domains.article.ArticleRepository;
import site.travellaboratory.be.infrastructure.domains.article.entity.Article;
import site.travellaboratory.be.infrastructure.domains.article.enums.ArticleStatus;
import site.travellaboratory.be.infrastructure.domains.bookmark.BookmarkRepository;
import site.travellaboratory.be.infrastructure.domains.bookmark.entity.Bookmark;
import site.travellaboratory.be.infrastructure.domains.bookmark.enums.BookmarkStatus;
import site.travellaboratory.be.infrastructure.domains.user.UserJpaRepository;
import site.travellaboratory.be.infrastructure.domains.user.entity.UserJpaEntity;
import site.travellaboratory.be.domain.user.enums.UserStatus;
import site.travellaboratory.be.presentation.auth.dto.userunregistration.UserUnregisterResponse;

@Service
@RequiredArgsConstructor
public class UserUnregistrationService {

    // todo: 회원이 작성했던 여행 계획, 리뷰 등등 다 비활성화처리필요함
    private final UserJpaRepository userJpaRepository;
    private final ArticleRepository articleRepository;
    private final BookmarkRepository bookmarkRepository;

    @Transactional
    public UserUnregisterResponse unregister(final Long userId) {
        UserJpaEntity userJpaEntity = userJpaRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        List<Article> articles = articleRepository.findByUserJpaEntityAndStatusIn(userJpaEntity,
                List.of(ArticleStatus.ACTIVE, ArticleStatus.PRIVATE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.ARTICLE_NOT_FOUND, HttpStatus.NOT_FOUND));

        articles.forEach(article -> article.updateStatus(ArticleStatus.INACTIVE));
        articleRepository.saveAll(articles);

        List<Bookmark> bookmarks = bookmarkRepository.findByUserJpaEntityAndStatusIn(userJpaEntity,
                List.of(BookmarkStatus.ACTIVE, BookmarkStatus.PRIVATE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.BOOKMARK_NOT_FOUND, HttpStatus.NOT_FOUND));

        bookmarks.forEach(bookmark -> bookmark.updateStatus(BookmarkStatus.INACTIVE));
        bookmarkRepository.saveAll(bookmarks);

        // 사용자 삭제 처리
        userJpaEntity.delete();
        userJpaRepository.save(userJpaEntity);

        return UserUnregisterResponse.from(true);
    }
}
