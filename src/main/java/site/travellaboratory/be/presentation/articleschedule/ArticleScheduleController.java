package site.travellaboratory.be.presentation.articleschedule;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.presentation.articleschedule.dto.ArticleScheduleReadPlacesResponse;
import site.travellaboratory.be.presentation.articleschedule.dto.ArticleScheduleUpdatePrivacyResponse;
import site.travellaboratory.be.presentation.articleschedule.dto.delete.ArticleScheduleDeleteResponse;
import site.travellaboratory.be.presentation.articleschedule.dto.get.ArticleScheduleReadDetailResponse;
import site.travellaboratory.be.presentation.articleschedule.dto.put.ArticleScheduleUpdateRequest;
import site.travellaboratory.be.presentation.articleschedule.dto.put.ArticleScheduleUpdateResponse;
import site.travellaboratory.be.application.ArticleScheduleService;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class ArticleScheduleController {

    private final ArticleScheduleService articleScheduleService;

    // 일정 리스트 조회
    @GetMapping("/articles/{articleId}/schedules")
    public ResponseEntity<ArticleScheduleReadDetailResponse> readDetailSchedules(
        @UserId Long userId,
        @PathVariable(name = "articleId") Long articleId
    ) {
        ArticleScheduleReadDetailResponse response = articleScheduleService.readSchedulesDetail(
            userId, articleId);
        return ResponseEntity.ok(response);
    }

    // 일정 리스트 전체 수정 - feat.(일정 [작성, 수정, 삭제])
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

    // 초기 여행 계획 + 일정 리스트 삭제
    @PatchMapping("/articles/{articleId}/status")
    public ResponseEntity<ArticleScheduleDeleteResponse> deleteArticleSchedules(
        @UserId Long userId,
        @PathVariable(name = "articleId") Long articleId
    ) {
        ArticleScheduleDeleteResponse response = articleScheduleService.deleteArticleSchedules(
            userId, articleId);
        return ResponseEntity.ok(response);
    }

    //
    @PatchMapping("/articles/{articleId}/privacy")
    public ResponseEntity<ArticleScheduleUpdatePrivacyResponse> updateArticlePrivacy(
        @UserId Long userId,
        @PathVariable(name = "articleId") Long articleId
    ) {
        ArticleScheduleUpdatePrivacyResponse response = articleScheduleService.updateArticlePrivacy(
            userId, articleId);
        return ResponseEntity.ok(response);
    }

    // 후기 작성 전 조회 - 여행 일정별 장소 리스트
    @GetMapping("/articles/{articleId}/schedules/places")
    public ResponseEntity<ArticleScheduleReadPlacesResponse> readSchedulesPlaces(
        @UserId Long userId,
        @PathVariable(name = "articleId") Long articleId
    ) {
        ArticleScheduleReadPlacesResponse response = articleScheduleService.readSchedulesPlaces(
            userId, articleId);
        return ResponseEntity.ok(response);
    }
}
