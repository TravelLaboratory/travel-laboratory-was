package site.travellaboratory.be.controller.bookmark;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.controller.bookmark.dto.BookmarkResponse;
import site.travellaboratory.be.service.BookmarkService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/{articleId}/bookmark")
    public ResponseEntity<Void> registerBookmark(@UserId final Long userId, @PathVariable final Long articleId) {
        bookmarkService.saveBookmark(userId, articleId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/bookmarks")
    public ResponseEntity<List<BookmarkResponse>> findMyAllBookmark(@UserId final Long userId) {
        final List<BookmarkResponse> allBookmarkByUser = bookmarkService.findAllBookmarkByUser(userId);
        return ResponseEntity.ok(allBookmarkByUser);
    }
}
