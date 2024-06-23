package site.travellaboratory.be.service;

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
import site.travellaboratory.be.controller.articleschedule.dto.ArticleScheduleReadPlacesResponse;
import site.travellaboratory.be.controller.articleschedule.dto.ArticleScheduleUpdatePrivacyResponse;
import site.travellaboratory.be.controller.articleschedule.dto.PlaceName;
import site.travellaboratory.be.controller.articleschedule.dto.SchedulePlace;
import site.travellaboratory.be.controller.articleschedule.dto.delete.ArticleScheduleDeleteResponse;
import site.travellaboratory.be.controller.articleschedule.dto.get.ArticleScheduleReadDetailResponse;
import site.travellaboratory.be.controller.articleschedule.dto.put.ArticleScheduleRequest;
import site.travellaboratory.be.controller.articleschedule.dto.put.ArticleScheduleUpdateResponse;
import site.travellaboratory.be.domain.article.Article;
import site.travellaboratory.be.domain.article.ArticleRepository;
import site.travellaboratory.be.domain.article.ArticleStatus;
import site.travellaboratory.be.domain.articleschedule.ArticleSchedule;
import site.travellaboratory.be.domain.articleschedule.ArticleScheduleRepository;
import site.travellaboratory.be.domain.articleschedule.ArticleScheduleStatus;
import site.travellaboratory.be.domain.articleschedule.dtype.ScheduleEtc;
import site.travellaboratory.be.domain.articleschedule.dtype.ScheduleGeneral;
import site.travellaboratory.be.domain.articleschedule.dtype.ScheduleTransport;
import site.travellaboratory.be.domain.review.Review;
import site.travellaboratory.be.domain.review.ReviewRepository;
import site.travellaboratory.be.domain.review.ReviewStatus;

@Service
@RequiredArgsConstructor
public class ArticleScheduleService {

    private final ArticleRepository articleRepository;
    private final ArticleScheduleRepository articleScheduleRepository;
    private final ReviewRepository reviewRepository;

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

    @Transactional
    public ArticleScheduleUpdateResponse updateSchedules(Long userId, Long articleId,
        List<ArticleScheduleRequest> requests) {
        // 유효하지 않은 여행 계획에 대한 상세 일정을 수정할 경우
        Article article = articleRepository.findByIdAndStatusIn(articleId, List.of(
                ArticleStatus.ACTIVE, ArticleStatus.PRIVATE))
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.ARTICLE_SCHEDULE_UPDATE_ARTICLE_INVALID,
                    HttpStatus.NOT_FOUND));

        // 유저가 작성한 초기 여행 계획이 아닌 경우
        if (!article.getUser().getId().equals(userId)) {
            throw new BeApplicationException(ErrorCodes.ARTICLE_SCHEDULE_UPDATE_NOT_USER,
                HttpStatus.FORBIDDEN);
        }

        // (0) 기존 일정들 불러오기 (삭제된 건 제외 + sortOrder로 내림차순) (N+1을 막기 위해 FETCH JOIN)
        List<ArticleSchedule> existingSchedules = articleScheduleRepository.findByArticleAndStatusOrderBySortOrderAsc(
            article, ArticleScheduleStatus.ACTIVE);
        // (1) 일정 수정 - id o , 일정 생성 - id x -> 이를 분리
        Map<Long, ArticleScheduleRequest> requestMap = requests.stream()
            .filter(request -> request.scheduleId() != null)
            // key - schedule id , value - schedule
            .collect(Collectors.toMap(ArticleScheduleRequest::scheduleId, request -> request));

        // (2) 삭제 처리 - 요청에 없음
        for (ArticleSchedule existingSchedule : existingSchedules) {
            // 기존 일정에는 있는데 요청에 없다면? 삭제 처리
            if (!requestMap.containsKey(existingSchedule.getId())) {
                existingSchedule.delete();
            }
        }

        // (3) 작성, 수정 처리
        for (ArticleScheduleRequest request : requests) {
            // id x -> 새로운 일정 생성 [INSERT]
            if (request.scheduleId() == null) {
                ArticleSchedule newSchedule = toArticleSchedule(article, request);
                articleScheduleRepository.save(newSchedule);
            }
            // id o -> 기존 일정 수정 [UPDATE]
            else {
                // 유효하지 않은 일정을 수정하려고 하는 경우
                ArticleSchedule existingSchedule = findScheduleById(existingSchedules,
                    request.scheduleId());
                updateExistingSchedule(existingSchedule, request);
            }
        }
        return ArticleScheduleUpdateResponse.from(article.getId());
    }

    @Transactional
    public ArticleScheduleDeleteResponse deleteArticleSchedules(Long userId, Long articleId) {
        // 유효하지 않은 초기 여행 계획(article_id) 을 삭제하려고 할 경우
        Article article = articleRepository.findByIdAndStatusIn(articleId, List.of(
                ArticleStatus.ACTIVE, ArticleStatus.PRIVATE))
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.ARTICLE_SCHEDULE_DELETE_INVALID,
                    HttpStatus.NOT_FOUND));

        // 유저가 작성한 초기 여행 계획(article_id)이 아닌 경우
        if (!article.getUser().getId().equals(userId)) {
            throw new BeApplicationException(ErrorCodes.ARTICLE_SCHEDULE_DELETE_NOT_USER,
                HttpStatus.FORBIDDEN);
        }

        // 초기 여행 계획 삭제
        article.delete();

        // 관련된 일정들 삭제
        List<ArticleSchedule> schedules = articleScheduleRepository.findByArticleAndStatusOrderByIdDesc(
            article, ArticleScheduleStatus.ACTIVE);
        for (ArticleSchedule schedule : schedules) {
            schedule.delete();
        }

        return ArticleScheduleDeleteResponse.from(true);
    }

    @Transactional
    public ArticleScheduleUpdatePrivacyResponse updateArticlePrivacy(Long userId, Long articleId) {
        // 유효하지 않은 초기 여행 계획(article_id) 의 수정(공개, 비공개)하려고 할 경우
        Article article = articleRepository.findByIdAndStatusIn(articleId, List.of(
                ArticleStatus.ACTIVE, ArticleStatus.PRIVATE))
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.ARTICLE_SCHEDULE_PRIVACY_INVALID,
                    HttpStatus.NOT_FOUND));

        // 유저가 작성한 초기 여행 계획(article_id)이 아닌 경우
        if (!article.getUser().getId().equals(userId)) {
            throw new BeApplicationException(ErrorCodes.ARTICLE_SCHEDULE_PRIVACY_NOT_USER,
                HttpStatus.FORBIDDEN);
        }

        // 초기 여행 계획 비공개 여부 수정
        article.togglePrivacyStatus();

        // 비공개 true, 공개 false
        boolean isPrivate = (article.getStatus() == ArticleStatus.PRIVATE);

        return ArticleScheduleUpdatePrivacyResponse.from(isPrivate);
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

    private ArticleSchedule toArticleSchedule(Article article, ArticleScheduleRequest request) {
        switch (request.dtype()) {
            case "GENERAL" -> {
                return ScheduleGeneral.of(
                    article,
                    request.visitedDate(),
                    request.visitedTime(),
                    request.sortOrder(),
                    request.category(),
                    request.durationTime(),
                    request.expense(),
                    request.memo(),
                    ArticleScheduleStatus.ACTIVE,
                    request.scheduleGeneral()
                );
            }
            // dtype - transport
            case "TRANSPORT" -> {
                System.out.println("transport");
                return ScheduleTransport.of(
                    article,
                    request.visitedDate(),
                    request.visitedTime(),
                    request.sortOrder(),
                    request.category(),
                    request.durationTime(),
                    request.expense(),
                    request.memo(),
                    ArticleScheduleStatus.ACTIVE,
                    request.scheduleTransport()
                );
            }
            case "ETC" -> {
                return ScheduleEtc.of(
                    article,
                    request.visitedDate(),
                    request.visitedTime(),
                    request.sortOrder(),
                    request.category(),
                    request.durationTime(),
                    request.expense(),
                    request.memo(),
                    ArticleScheduleStatus.ACTIVE,
                    request.scheduleEtc()
                );
            }
            // dtype 에 general, transport, etc 외에 다른 값이 포함되어 있는 경우
            default -> throw new BeApplicationException(ErrorCodes.ARTICLE_SCHEDULE_POST_NOT_DTYPE,
                HttpStatus.BAD_REQUEST);
        }
    }

    private ArticleSchedule findScheduleById(List<ArticleSchedule> schedules, Long id) {
        // 유효하지 않은 일정을 수정하려고 하는 경우
        return schedules.stream()
            .filter(schedule -> schedule.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new BeApplicationException(
                ErrorCodes.ARTICLE_SCHEDULE_UPDATE_SCHEDULE_INVALID, HttpStatus.NOT_FOUND));
    }

    private void updateExistingSchedule(ArticleSchedule existingSchedule,
        ArticleScheduleRequest request) {
        if (existingSchedule instanceof ScheduleGeneral) {
            ((ScheduleGeneral) existingSchedule).update(request.scheduleGeneral());
        } else if (existingSchedule instanceof ScheduleTransport) {
            ((ScheduleTransport) existingSchedule).update(request.scheduleTransport());
        } else if (existingSchedule instanceof ScheduleEtc) {
            ((ScheduleEtc) existingSchedule).update(request.scheduleEtc());
        }
        existingSchedule.update(request);
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
