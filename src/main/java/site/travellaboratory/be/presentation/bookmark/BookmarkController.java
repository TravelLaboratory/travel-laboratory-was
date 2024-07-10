package site.travellaboratory.be.presentation.bookmark;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.presentation.bookmark.dto.BookmarkResponse;
import site.travellaboratory.be.presentation.bookmark.dto.BookmarkSaveResponse;
import site.travellaboratory.be.application.BookmarkService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PatchMapping("/bookmark/{articleId}")
    public ResponseEntity<BookmarkSaveResponse> registerBookmark(
            @UserId final Long userId,
            @PathVariable(name = "articleId") final Long articleId
    ) {
        final BookmarkSaveResponse bookmarkSaveResponse = bookmarkService.saveBookmark(userId, articleId);
        return ResponseEntity.ok(bookmarkSaveResponse);
    }

    @GetMapping("/bookmarks/{userId}")
    public ResponseEntity<Page<BookmarkResponse>> findMyAllBookmark(
            @UserId final Long loginId,
            @PathVariable(name = "userId") final Long userId,
            @RequestParam(defaultValue = "0", value = "page") int page,
            @RequestParam(defaultValue = "10", value = "size") int size
    ) {
        final Page<BookmarkResponse> allBookmarkByUser = bookmarkService.findAllBookmarkByUser(loginId,userId,
                PageRequest.of(page, size));

        return ResponseEntity.ok(allBookmarkByUser);
    }
}
