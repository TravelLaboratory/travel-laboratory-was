package site.travellaboratory.be.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.travellaboratory.be.controller.bookmark.dto.BookmarkResponse;
import site.travellaboratory.be.domain.article.Article;
import site.travellaboratory.be.domain.article.ArticleRepository;
import site.travellaboratory.be.domain.bookmark.Bookmark;
import site.travellaboratory.be.domain.bookmark.BookmarkRepository;
import site.travellaboratory.be.domain.user.entity.User;
import site.travellaboratory.be.domain.user.UserRepository;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    public Long saveBookmark(final Long userId, final Long articleId) {
        final User user = userRepository.getById(userId);
        final Article article = articleRepository.getById(articleId);
        final Bookmark bookMark = Bookmark.of(user, article);
        bookmarkRepository.save(bookMark);
        return bookMark.getId();
    }

    public List<BookmarkResponse> findAllBookmarkByUser(final Long userId) {
        final User user = userRepository.getById(userId);
        final List<Bookmark> bookmarkByUser = bookmarkRepository.findBookmarkByUser(user);
        return BookmarkResponse.from(bookmarkByUser);
    }
}
