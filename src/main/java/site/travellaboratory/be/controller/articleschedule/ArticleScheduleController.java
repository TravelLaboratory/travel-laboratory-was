package site.travellaboratory.be.controller.articleschedule;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.controller.articleschedule.dto.ArticleScheduleDeleteResponse;
import site.travellaboratory.be.controller.articleschedule.dto.ArticleScheduleUpdateRequest;
import site.travellaboratory.be.controller.articleschedule.dto.ArticleScheduleUpdateResponse;
import site.travellaboratory.be.service.ArticleScheduleService;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class ArticleScheduleController {

    private final ArticleScheduleService articleScheduleService;

    @PutMapping("/articles/{articleId}/schedules")
    public ResponseEntity<ArticleScheduleUpdateResponse> updateArticleSchedules(
        @UserId Long userId,
        @PathVariable(name = "articleId") Long articleId,
        @RequestBody ArticleScheduleUpdateRequest articleScheduleUpdateRequest
    ) {
        ArticleScheduleUpdateResponse response = articleScheduleService.updateSchedules(
            userId, articleId,
            articleScheduleUpdateRequest.schedules());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/articles/{articleId}/status")
    public ResponseEntity<ArticleScheduleDeleteResponse> deleteArticleSchedules(
        @UserId Long userId,
        @PathVariable(name = "articleId") Long articleId
    ) {
        ArticleScheduleDeleteResponse response = articleScheduleService.deleteArticleSchedules(
            userId, articleId);
        return ResponseEntity.ok(response);
    }
}
