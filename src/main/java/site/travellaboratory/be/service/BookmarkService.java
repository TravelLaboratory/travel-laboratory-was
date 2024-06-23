package site.travellaboratory.be.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.controller.bookmark.dto.BookmarkResponse;
import site.travellaboratory.be.controller.bookmark.dto.BookmarkSaveResponse;
import site.travellaboratory.be.domain.article.Article;
import site.travellaboratory.be.domain.article.ArticleRepository;
import site.travellaboratory.be.domain.article.ArticleStatus;
import site.travellaboratory.be.domain.bookmark.Bookmark;
import site.travellaboratory.be.domain.bookmark.BookmarkRepository;
import site.travellaboratory.be.domain.bookmark.BookmarkStatus;
import site.travellaboratory.be.domain.user.UserRepository;
import site.travellaboratory.be.domain.user.entity.User;
import site.travellaboratory.be.domain.user.entity.UserStatus;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    @Transactional
    public BookmarkSaveResponse saveBookmark(final Long userId, final Long articleId) {
        final User user = userRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND,
                        HttpStatus.NOT_FOUND));

        final Article article = articleRepository.findByIdAndStatusIn(articleId,
                        List.of(ArticleStatus.ACTIVE, ArticleStatus.PRIVATE))
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.ARTICLE_NOT_FOUND, HttpStatus.NOT_FOUND));

        Bookmark bookmark = bookmarkRepository.findByArticleAndUser(article, user).orElse(null);

        if (bookmark != null) {
            bookmark.toggleStatus();
        } else {
            bookmark = Bookmark.of(user, article);
        }

        final Bookmark newBookmark = bookmarkRepository.save(bookmark);
        return BookmarkSaveResponse.from(newBookmark);
    }

    @Transactional
    public Page<BookmarkResponse> findAllBookmarkByUser(final Long loginId, final Long userId, Pageable pageable) {
        final User loginUser = userRepository.findByIdAndStatus(loginId, UserStatus.ACTIVE)
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND,
                        HttpStatus.NOT_FOUND));

        final User user = userRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND,
                        HttpStatus.NOT_FOUND));

        final Page<Bookmark> bookmarks = bookmarkRepository.findByUserAndStatusIn(user,
                        List.of(BookmarkStatus.ACTIVE), pageable)
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.BOOKMARK_NOT_FOUND, HttpStatus.NOT_FOUND));

        List<BookmarkResponse> bookmarkResponses = bookmarks.stream()
                .map(bookmark -> {
                    final Long bookmarkCount = bookmarkRepository.countByArticleIdAndStatus(bookmark.getArticle().getId(),
                            BookmarkStatus.ACTIVE);
                    boolean isBookmarked = bookmarkRepository.existsByUserIdAndArticleIdAndStatus(
                            loginUser.getId(), bookmark.getArticle().getId(),
                            BookmarkStatus.ACTIVE);

                    return BookmarkResponse.of(
                            bookmark,
                            bookmarkCount,
                            isBookmarked
                    );
                })
                .toList();
        return new PageImpl<>(bookmarkResponses, pageable, bookmarks.getTotalElements());
    }
}

