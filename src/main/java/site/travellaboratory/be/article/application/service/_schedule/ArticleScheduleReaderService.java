package site.travellaboratory.be.article.application.service._schedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.article.domain._schedule.enums.ArticleScheduleStatus;
import site.travellaboratory.be.article.domain._views.ArticleViews;
import site.travellaboratory.be.article.domain.enums.ArticleStatus;
import site.travellaboratory.be.article.infrastructure.persistence.entity.ArticleEntity;
import site.travellaboratory.be.article.infrastructure.persistence.entity.ArticleViewsEntity;
import site.travellaboratory.be.article.infrastructure.persistence.entity._schedule.ArticleScheduleEntity;
import site.travellaboratory.be.article.infrastructure.persistence.entity._schedule.ScheduleEtcEntity;
import site.travellaboratory.be.article.infrastructure.persistence.entity._schedule.ScheduleGeneralEntity;
import site.travellaboratory.be.article.infrastructure.persistence.entity._schedule.ScheduleTransportEntity;
import site.travellaboratory.be.article.infrastructure.persistence.repository.ArticleJpaRepository;
import site.travellaboratory.be.article.infrastructure.persistence.repository.ArticleViewsJpaRepository;
import site.travellaboratory.be.article.infrastructure.persistence.repository._schedule.ArticleScheduleJpaRepository;
import site.travellaboratory.be.article.presentation.response._schedule.reader.ArticleScheduleReadDetailResponse;
import site.travellaboratory.be.article.presentation.response._schedule.reader.ArticleScheduleReadPlacesResponse;
import site.travellaboratory.be.article.presentation.response._schedule.reader.PlaceName;
import site.travellaboratory.be.article.presentation.response._schedule.reader.SchedulePlace;
import site.travellaboratory.be.common.error.ErrorCodes;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.review.domain.enums.ReviewStatus;
import site.travellaboratory.be.review.infrastructure.persistence.entity.ReviewEntity;
import site.travellaboratory.be.review.infrastructure.persistence.repository.ReviewJpaRepository;

@Service
@RequiredArgsConstructor
public class ArticleScheduleReaderService {

    private final ArticleJpaRepository articleJpaRepository;
    private final ArticleScheduleJpaRepository articleScheduleJpaRepository;
    private final ReviewJpaRepository reviewJpaRepository;
    private final ArticleViewsJpaRepository articleViewsJpaRepository;

    /*
     * GET - /api/v1/articles/{articleId}/schedules
     * 일정 상세 - 일정 리스트 조회
     * */
    @Transactional
    public ArticleScheduleReadDetailResponse readSchedulesDetail(Long userId, Long articleId) {
        // 유효하지 않은 여행 계획을 조회할 경우
        ArticleEntity articleEntity = getArticleEntityById(articleId);

        // ReviewId 조회
        Long reviewId = getReviewIdByArticleId(articleEntity.getId());

        // 조회수 관련
        recordViewCount(userId, articleId);

        // 일정 리스트 조회
        List<ArticleScheduleEntity> schedules = articleScheduleJpaRepository.findByArticleEntityAndStatusOrderBySortOrderAsc(
            articleEntity, ArticleScheduleStatus.ACTIVE);

        boolean isEditable = articleEntity.getUserEntity().getId().equals(userId);

        return ArticleScheduleReadDetailResponse.from(reviewId, isEditable, schedules);
    }

    /*
     * GET - /api/v1/articles/{articleId}/schedules/places
     * 후기 작성 전 조회 - 여행 일정별 장소 리스트
     * */
    @Transactional(readOnly = true)
    public ArticleScheduleReadPlacesResponse readSchedulesPlaces(Long userId, Long articleId) {
        // 유효하지 않은 여행 계획을 조회할 경우
        ArticleEntity articleEntity = articleJpaRepository.findByIdAndStatus(articleId,
                ArticleStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.REVIEW_BEFORE_POST_INVALID,
                HttpStatus.NOT_FOUND));

        // 유저가 작성한 article_id이 아닌 경우
        if (!articleEntity.getUserEntity().getId().equals(userId)) {
            throw new BeApplicationException(ErrorCodes.REVIEW_BEFORE_POST_NOT_USER,
                HttpStatus.FORBIDDEN);
        }

        // 이미 해당 여행 계획에 대한 후기가 있을 경우
        reviewJpaRepository.findByArticleEntityIdAndStatus(
                articleEntity.getId(), ReviewStatus.ACTIVE)
            .ifPresent(it -> {
                throw new BeApplicationException(ErrorCodes.REVIEW_BEFORE_POST_EXIST,
                    HttpStatus.CONFLICT);
            });

        // 일정 리스트 조회
        List<ArticleScheduleEntity> schedules = articleScheduleJpaRepository.findByArticleEntityAndStatusOrderBySortOrderAsc(
            articleEntity,
            ArticleScheduleStatus.ACTIVE);

        // 초기 여행 계획만 있어서, 상세 일정이 빈 리스트인 경우
        if (schedules.isEmpty()) {
            throw new BeApplicationException(ErrorCodes.REVIEW_BEFORE_POST_NOT_EXIST_SCHEDULES,
                HttpStatus.NOT_FOUND);
        }

        // 1순위 방문날짜, 2순위 sortOrder로 정렬
        List<ArticleScheduleEntity> sortedSchedules = schedules.stream()
            .sorted(Comparator.comparing(ArticleScheduleEntity::getVisitedDate)
                .thenComparing(ArticleScheduleEntity::getSortOrder))
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
    private Stream<String> getPlaceNames(ArticleScheduleEntity schedule) {
        if (schedule instanceof ScheduleGeneralEntity) {
            return Stream.of(((ScheduleGeneralEntity) schedule).getPlaceName());
        } else if (schedule instanceof ScheduleTransportEntity) {
            return Stream.of(
                ((ScheduleTransportEntity) schedule).getStartPlaceName(),
                ((ScheduleTransportEntity) schedule).getEndPlaceName()
            );
        } else if (schedule instanceof ScheduleEtcEntity) {
            return Stream.of(((ScheduleEtcEntity) schedule).getPlaceName());
        }
        return Stream.empty();
    }

    private void recordViewCount(Long userId, Long articleId) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        // 오늘 해당 사용자가 이미 조회했는지 확인
        Optional<ArticleViewsEntity> optionalArticleViewsEntity = articleViewsJpaRepository.findByUserIdAndArticleIdAndCreatedAtBetween(
            userId, articleId, startOfDay, endOfDay);

        final ArticleViews articleViews = optionalArticleViewsEntity
            .map(ArticleViewsEntity::toModel)
            .map(ArticleViews::withUpdatedAt)
            .orElseGet(() -> ArticleViews.create(userId, articleId));

        articleViewsJpaRepository.save(ArticleViewsEntity.from(articleViews));
    }

    private ArticleEntity getArticleEntityById(Long articleId) {
        return articleJpaRepository.findByIdAndStatus(articleId, ArticleStatus.ACTIVE)
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.ARTICLE_SCHEDULE_READ_DETAIL_INVALID,
                    HttpStatus.NOT_FOUND));
    }

    @Nullable
    private Long getReviewIdByArticleId(Long articleId) {
        return reviewJpaRepository.findByArticleEntityIdAndStatus(articleId, ReviewStatus.ACTIVE)
            .map(ReviewEntity::getId)
            .orElse(null);
    }

}
