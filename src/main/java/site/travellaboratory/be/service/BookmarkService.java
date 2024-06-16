package site.travellaboratory.be.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import site.travellaboratory.be.BeApplication;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.controller.bookmark.dto.BookmarkResponse;
import site.travellaboratory.be.controller.bookmark.dto.BookmarkSaveResponse;
import site.travellaboratory.be.controller.review.dto.userlikereview.ReviewToggleLikeResponse;
import site.travellaboratory.be.domain.article.Article;
import site.travellaboratory.be.domain.article.ArticleRepository;
import site.travellaboratory.be.domain.article.ArticleStatus;
import site.travellaboratory.be.domain.bookmark.Bookmark;
import site.travellaboratory.be.domain.bookmark.BookmarkRepository;
import site.travellaboratory.be.domain.bookmark.BookmarkStatus;
import site.travellaboratory.be.domain.review.ReviewStatus;
import site.travellaboratory.be.domain.user.entity.User;
import site.travellaboratory.be.domain.user.UserRepository;
import site.travellaboratory.be.domain.user.entity.UserStatus;
import site.travellaboratory.be.domain.userlikereview.UserLikeReview;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

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

    public List<BookmarkResponse> findAllBookmarkByUser(final Long userId) {
        final User user = userRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND,
                        HttpStatus.NOT_FOUND));

        final List<Bookmark> bookmarkByUser = bookmarkRepository.findByUserAndStatus(user, BookmarkStatus.ACTIVE);
        return BookmarkResponse.from(bookmarkByUser);
    }
}
