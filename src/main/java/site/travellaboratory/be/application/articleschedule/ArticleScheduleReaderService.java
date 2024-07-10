package site.travellaboratory.be.application.articleschedule;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.infrastructure.article.ArticleRepository;
import site.travellaboratory.be.infrastructure.article.entity.Article;
import site.travellaboratory.be.infrastructure.article.enums.ArticleStatus;
import site.travellaboratory.be.infrastructure.articleschedule.ArticleSchedule;
import site.travellaboratory.be.infrastructure.articleschedule.ArticleScheduleRepository;
import site.travellaboratory.be.infrastructure.articleschedule.ArticleScheduleStatus;
import site.travellaboratory.be.infrastructure.articleschedule.dtype.ScheduleEtc;
import site.travellaboratory.be.infrastructure.articleschedule.dtype.ScheduleGeneral;
import site.travellaboratory.be.infrastructure.articleschedule.dtype.ScheduleTransport;
import site.travellaboratory.be.infrastructure.review.ReviewRepository;
import site.travellaboratory.be.infrastructure.review.entity.Review;
import site.travellaboratory.be.infrastructure.review.enums.ReviewStatus;
import site.travellaboratory.be.presentation.articleschedule.dto.reader.ArticleScheduleReadPlacesResponse;
import site.travellaboratory.be.presentation.articleschedule.dto.reader.PlaceName;
import site.travellaboratory.be.presentation.articleschedule.dto.reader.SchedulePlace;
import site.travellaboratory.be.presentation.articleschedule.dto.reader.ArticleScheduleReadDetailResponse;

@Service
@RequiredArgsConstructor
public class ArticleScheduleReaderService
{
    private final ArticleRepository articleRepository;
    private final ArticleScheduleRepository articleScheduleRepository;
    private final ReviewRepository reviewRepository;

    /*
    * GET - /api/v1/articles/{articleId}/schedules
    * 일정 상세 - 일정 리스트 조회
    * */
    @Transactional(readOnly = true)
    public ArticleScheduleReadDetailResponse readSchedulesDetail(Long userId, Long articleId) {
        // 유효하지 않은 여행 계획을 조회할 경우
        Article article = articleRepository.findByIdAndStatusIn(articleId, List.of(
                ArticleStatus.ACTIVE, ArticleStatus.PRIVATE))
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.ARTICLE_SCHEDULE_READ_DETAIL_INVALID,
                    HttpStatus.NOT_FOUND));

        // 나만보기 상태의 여행 계획을 다른 유저가 조회할 경우
        if (article.getStatus() == ArticleStatus.PRIVATE && !article.getUser().getId()
            .equals(userId)) {
            throw new BeApplicationException(ErrorCodes.ARTICLE_SCHEDULE_READ_DETAIL_NOT_USER,
                HttpStatus.FORBIDDEN);
        }

        // reviewId 찾아오기 없다면 null
        Long reviewId = reviewRepository.findByArticleAndStatus(article, ReviewStatus.ACTIVE)
            .map(Review::getId)
            .orElse(null);

        boolean isEditable = article.getUser().getId().equals(userId);

        System.out.println("조회 시작");
        // 일정 리스트 조회
        List<ArticleSchedule> schedules = articleScheduleRepository.findByArticleAndStatusOrderBySortOrderAsc(
            article, ArticleScheduleStatus.ACTIVE);

        return ArticleScheduleReadDetailResponse.from(reviewId, isEditable, schedules);
    }

    /*
     * GET - /api/v1/articles/{articleId}/schedules/places
     * 후기 작성 전 조회 - 여행 일정별 장소 리스트
     * */
    @Transactional(readOnly = true)
    public ArticleScheduleReadPlacesResponse readSchedulesPlaces(Long userId, Long articleId) {
        // 유효하지 않은 여행 계획을 조회할 경우
        Article article = articleRepository.findByIdAndStatusIn(articleId, List.of(
                ArticleStatus.ACTIVE, ArticleStatus.PRIVATE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.REVIEW_BEFORE_POST_INVALID,
                HttpStatus.NOT_FOUND));

        // 유저가 작성한 article_id이 아닌 경우
        if (!article.getUser().getId().equals(userId)) {
            throw new BeApplicationException(ErrorCodes.REVIEW_BEFORE_POST_NOT_USER,
                HttpStatus.FORBIDDEN);
        }

        // 이미 해당 여행 계획에 대한 후기가 있을 경우
        reviewRepository.findByArticleAndStatusInOrderByArticleDesc(article,
                List.of(ReviewStatus.ACTIVE, ReviewStatus.PRIVATE))
            .ifPresent(it -> {
                throw new BeApplicationException(ErrorCodes.REVIEW_BEFORE_POST_EXIST,
                    HttpStatus.CONFLICT);
            });

        // 일정 리스트 조회
        List<ArticleSchedule> schedules = articleScheduleRepository.findByArticleAndStatusOrderBySortOrderAsc(
            article,
            ArticleScheduleStatus.ACTIVE);

        // 초기 여행 계획만 있어서, 상세 일정이 빈 리스트인 경우
        if (schedules.isEmpty()) {
            throw new BeApplicationException(ErrorCodes.REVIEW_BEFORE_POST_NOT_EXIST_SCHEDULES,
                HttpStatus.NOT_FOUND);
        }

        // 1순위 방문날짜, 2순위 sortOrder로 정렬
        List<ArticleSchedule> sortedSchedules = schedules.stream()
            .sorted(Comparator.comparing(ArticleSchedule::getVisitedDate)
                .thenComparing(ArticleSchedule::getSortOrder))
            .toList();

        // 방문날짜별 그룹화
        Map<String, List<String>> placesByDate = sortedSchedules.stream()
            .collect(Collectors.groupingBy(
                schedule -> schedule.getVisitedDate().toString(), // 방문날짜 기준으로 그룹화
                LinkedHashMap::new, // 순서를 유지하기 위해 LinkedHashMap 사용
                Collectors.flatMapping(this::getPlaceNames, Collectors.toList())
            ));

        // Map -> List로
        List<SchedulePlace> schedulePlaces = placesByDate.entrySet().stream()
            .map(entry -> new SchedulePlace(
                entry.getKey(),  // 방문날짜
                entry.getValue().stream()
                    .map(PlaceName::new)
                    .collect(Collectors.toList())))
            .toList();

        return ArticleScheduleReadPlacesResponse.from(articleId, schedulePlaces);
    }

    // GENERAL, ETC - 장소명만 존재
    // TRANSPORT - 출발지명, 도착지명이 존재
    private Stream<String> getPlaceNames(ArticleSchedule schedule) {
        if (schedule instanceof ScheduleGeneral) {
            return Stream.of(((ScheduleGeneral) schedule).getPlaceName());
        } else if (schedule instanceof ScheduleTransport) {
            return Stream.of(
                ((ScheduleTransport) schedule).getStartPlaceName(),
                ((ScheduleTransport) schedule).getEndPlaceName()
            );
        } else if (schedule instanceof ScheduleEtc) {
            return Stream.of(((ScheduleEtc) schedule).getPlaceName());
        }
        return Stream.empty();
    }
}
