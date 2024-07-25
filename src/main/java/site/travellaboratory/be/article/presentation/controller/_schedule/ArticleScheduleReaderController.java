package site.travellaboratory.be.article.presentation.controller._schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.article.application.service._schedule.ArticleScheduleReaderService;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.article.presentation.response._schedule.reader.ArticleScheduleReadPlacesResponse;
import site.travellaboratory.be.article.presentation.response._schedule.reader.ArticleScheduleReadDetailResponse;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class ArticleScheduleReaderController {

    private final ArticleScheduleReaderService articleScheduleReaderService;

    // 일정 리스트 조회
    @GetMapping("/articles/{articleId}/schedules")
    public ResponseEntity<ArticleScheduleReadDetailResponse> readDetailSchedules(
        @UserId Long userId,
        @PathVariable(name = "articleId") Long articleId
    ) {
        ArticleScheduleReadDetailResponse response = articleScheduleReaderService.readSchedulesDetail(
            userId, articleId);
        return ResponseEntity.ok(response);
    }

    // 후기 작성 전 조회 - 여행 일정별 장소 리스트
    @GetMapping("/articles/{articleId}/schedules/places")
    public ResponseEntity<ArticleScheduleReadPlacesResponse> readSchedulesPlaces(
        @UserId Long userId,
        @PathVariable(name = "articleId") Long articleId
    ) {
        ArticleScheduleReadPlacesResponse response = articleScheduleReaderService.readSchedulesPlaces(
            userId, articleId);
        return ResponseEntity.ok(response);
    }
}
