package site.travellaboratory.be.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.controller.articleschedule.dto.ArticleScheduleDeleteResponse;
import site.travellaboratory.be.controller.articleschedule.dto.ArticleScheduleRequest;
import site.travellaboratory.be.controller.articleschedule.dto.ArticleScheduleUpdateResponse;
import site.travellaboratory.be.domain.article.Article;
import site.travellaboratory.be.domain.article.ArticleRepository;
import site.travellaboratory.be.domain.article.ArticleStatus;
import site.travellaboratory.be.domain.articleschedule.ArticleSchedule;
import site.travellaboratory.be.domain.articleschedule.ArticleScheduleRepository;
import site.travellaboratory.be.domain.articleschedule.ArticleScheduleStatus;
import site.travellaboratory.be.domain.articleschedule.dtype.ScheduleEtc;
import site.travellaboratory.be.domain.articleschedule.dtype.ScheduleGeneral;
import site.travellaboratory.be.domain.articleschedule.dtype.ScheduleTransport;

@Service
@RequiredArgsConstructor
public class ArticleScheduleService {

    private final ArticleRepository articleRepository;
    private final ArticleScheduleRepository articleScheduleRepository;

    @Transactional
    public ArticleScheduleUpdateResponse updateSchedules(Long userId, Long articleId,
        List<ArticleScheduleRequest> requests) {
        // 유효하지 않은 여행 계획에 대한 상세 일정을 수정할 경우
        Article article = articleRepository.findByIdAndStatusIn(articleId, List.of(
                ArticleStatus.ACTIVE, ArticleStatus.PRIVATE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.ARTICLE_SCHEDULE_UPDATE_ARTICLE_INVALID,
                HttpStatus.NOT_FOUND));

        // 유저가 작성한 초기 여행 계획이 아닌 경우
        if (!article.getUser().getId().equals(userId)) {
            throw new BeApplicationException(ErrorCodes.ARTICLE_SCHEDULE_UPDATE_NOT_USER,
                HttpStatus.FORBIDDEN);
        }

        // (0) 기존 일정들 불러오기 (삭제된 건 제외 + sortOrder로 내림차순) (N+1을 막기 위해 FETCH JOIN)
        List<ArticleSchedule> existingSchedules = articleScheduleRepository.findByArticleAndStatusOrderBySortOrderAscFetchJoinSchedules(
            article, ArticleScheduleStatus.ACTIVE);

        // (1) 일정 수정 - id o , 일정 생성 - id x -> 이를 분리
        Map<Long, ArticleScheduleRequest> requestMap = requests.stream()
            .filter(request -> request.id() != null)
            // key - schedule id , value - schedule
            .collect(Collectors.toMap(ArticleScheduleRequest::id, request -> request));

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
            if (request.id() == null) {
                ArticleSchedule newSchedule = toArticleSchedule(article, request);
                articleScheduleRepository.save(newSchedule);
            }
            // id o -> 기존 일정 수정 [UPDATE]
            else {
                // 유효하지 않은 일정을 수정하려고 하는 경우
                ArticleSchedule existingSchedule = findScheduleById(existingSchedules, request.id());
                updateExistingSchedule(existingSchedule, request);
            }
        }
        return ArticleScheduleUpdateResponse.from(article.getId());
    }

    //
    @Transactional
    public ArticleScheduleDeleteResponse deleteArticleSchedules(Long userId, Long articleId) {
        // 유효하지 않은 초기 여행 계획(article_id) 을 삭제하려고 할 경우
        Article article = articleRepository.findByIdAndStatusIn(articleId, List.of(
                ArticleStatus.ACTIVE, ArticleStatus.PRIVATE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.ARTICLE_SCHEDULE_DELETE_INVALID,
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
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.ARTICLE_SCHEDULE_UPDATE_SCHEDULE_INVALID, HttpStatus.NOT_FOUND));
    }

    private void updateExistingSchedule(ArticleSchedule existingSchedule, ArticleScheduleRequest request) {
        if (existingSchedule instanceof ScheduleGeneral) {
            ((ScheduleGeneral) existingSchedule).update(request.scheduleGeneral());
        } else if (existingSchedule instanceof ScheduleTransport) {
            ((ScheduleTransport) existingSchedule).update(request.scheduleTransport());
        } else if (existingSchedule instanceof ScheduleEtc) {
            ((ScheduleEtc) existingSchedule).update(request.scheduleEtc());
        }
        existingSchedule.update(request);
    }
}
