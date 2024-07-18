package site.travellaboratory.be.article.presentation.controller._schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.article.application._schedule.ArticleScheduleWriterService;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.article.presentation.response._schedule.writer.ArticleScheduleDeleteResponse;
import site.travellaboratory.be.article.domain._schedule.request.ArticleScheduleUpdateRequest;
import site.travellaboratory.be.article.presentation.response._schedule.writer.ArticleScheduleUpdateResponse;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class ArticleScheduleWriterController {

    private final ArticleScheduleWriterService articleScheduleWriterService;

    // 일정 리스트 전체 수정 - feat.(일정 [작성, 수정, 삭제])
    @PutMapping("/articles/{articleId}/schedules")
    public ResponseEntity<ArticleScheduleUpdateResponse> updateArticleSchedules(
        @UserId Long userId,
        @PathVariable(name = "articleId") Long articleId,
        @RequestBody ArticleScheduleUpdateRequest articleScheduleUpdateRequest
    ) {
        ArticleScheduleUpdateResponse response = articleScheduleWriterService.updateSchedules(
            userId, articleId,
            articleScheduleUpdateRequest.schedules());
        return ResponseEntity.ok(response);
    }

    // 초기 여행 계획 + 일정 리스트 삭제
    @PatchMapping("/articles/{articleId}/status")
    public ResponseEntity<ArticleScheduleDeleteResponse> deleteArticleSchedules(
        @UserId Long userId,
        @PathVariable(name = "articleId") Long articleId
    ) {
        ArticleScheduleDeleteResponse response = articleScheduleWriterService.deleteArticleSchedules(
            userId, articleId);
        return ResponseEntity.ok(response);
    }
}
