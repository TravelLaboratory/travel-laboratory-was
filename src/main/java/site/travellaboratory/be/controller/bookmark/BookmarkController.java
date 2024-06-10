package site.travellaboratory.be.controller.bookmark;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.controller.bookmark.dto.BookmarkResponse;
import site.travellaboratory.be.service.BookmarkService;

@RestController
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/api/v1/users/{userId}/{articleId}/bookmark")
    public ResponseEntity<Long> saveBookmark(@PathVariable final Long userId, @PathVariable final Long articleId) {
        final   Long bookmarkId = bookmarkService.saveBookmark(userId, articleId);
        return ResponseEntity.created(URI.create("/user/bookmark/" + bookmarkId)).build();
    }

    @GetMapping("/api/v1/users/{userId}/bookmarks")
    public ResponseEntity<List<BookmarkResponse>> findAllBookMarkByUser(@PathVariable final Long userId) {
        final List<BookmarkResponse> allBookmarkByUser = bookmarkService.findAllBookmarkByUser(userId);
        return ResponseEntity.ok(allBookmarkByUser);
    }
}
