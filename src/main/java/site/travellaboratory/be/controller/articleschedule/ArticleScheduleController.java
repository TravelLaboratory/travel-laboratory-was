package site.travellaboratory.be.controller.articleschedule;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.controller.articleschedule.dto.ArticleScheduleSaveRequest;
import site.travellaboratory.be.controller.articleschedule.dto.ArticleScheduleSaveResponse;
import site.travellaboratory.be.service.ArticleScheduleService;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class ArticleScheduleController {

    private final ArticleScheduleService articleScheduleService;

    @PostMapping("/articles/{articleId}/schedules")
    public ResponseEntity<ArticleScheduleSaveResponse> saveArticleSchedules(
        @UserId Long userId,
        @PathVariable(name = "articleId") Long articleId,
        @RequestBody ArticleScheduleSaveRequest articleScheduleRequests
    ) {
        ArticleScheduleSaveResponse response = articleScheduleService.saveSchedules(
            userId, articleId, articleScheduleRequests.schedules());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
