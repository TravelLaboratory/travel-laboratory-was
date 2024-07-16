package site.travellaboratory.be.application.articleschedule;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.domain.article.Article;
import site.travellaboratory.be.domain.article.ArticleSchedule;
import site.travellaboratory.be.domain.article.enums.ArticleScheduleStatus;
import site.travellaboratory.be.domain.article.enums.ArticleStatus;
import site.travellaboratory.be.domain.user.enums.UserStatus;
import site.travellaboratory.be.domain.user.user.User;
import site.travellaboratory.be.infrastructure.domains.article.ArticleJpaRepository;
import site.travellaboratory.be.infrastructure.domains.article.entity.ArticleJpaEntity;
import site.travellaboratory.be.infrastructure.domains.articleschedule.entity.ArticleScheduleJpaEntity;
import site.travellaboratory.be.infrastructure.domains.articleschedule.repository.ArticleScheduleJpaRepository;
import site.travellaboratory.be.infrastructure.domains.user.UserJpaRepository;
import site.travellaboratory.be.presentation.articleschedule.dto.writer.ArticleScheduleDeleteResponse;
import site.travellaboratory.be.presentation.articleschedule.dto.writer.ArticleScheduleRequest;
import site.travellaboratory.be.presentation.articleschedule.dto.writer.ArticleScheduleUpdateResponse;

@Service
@RequiredArgsConstructor
public class ArticleScheduleWriterService {

    private final ArticleJpaRepository articleJpaRepository;
    private final ArticleScheduleJpaRepository articleScheduleJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Transactional
    public ArticleScheduleUpdateResponse updateSchedules(Long userId, Long articleId,
        List<ArticleScheduleRequest> requests) {
        // userId로 User 가져오기 - tokenId로 체크
        User user = userJpaRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND,
                HttpStatus.NOT_FOUND)).toModel();

        // 유효하지 않은 여행 계획에 대한 상세 일정을 수정할 경우
        ArticleJpaEntity articleJpaEntity = articleJpaRepository.findByIdAndStatusIn(articleId,
                List.of(
                    ArticleStatus.ACTIVE, ArticleStatus.PRIVATE))
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.ARTICLE_SCHEDULE_UPDATE_ARTICLE_INVALID,
                    HttpStatus.NOT_FOUND));

        // 유저가 작성한 초기 여행 계획이 아닌 경우
        Article article = articleJpaEntity.toModel();
        article.verifyOwner(user);

        // (0) 기존 일정들 불러오기 (삭제된 건 제외 + sortOrder로 내림차순) (N+1을 막기 위해 FETCH JOIN)
        List<ArticleSchedule> existingSchedules = articleScheduleJpaRepository.findByArticleJpaEntityAndStatusOrderBySortOrderAsc(
                articleJpaEntity, ArticleScheduleStatus.ACTIVE).stream()
            .map(ArticleScheduleJpaEntity::toModel)
            .toList();

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
            ArticleSchedule articleSchedule;
            if (request.scheduleId() == null) {
                // id x -> 새로운 일정 생성
                articleSchedule = ArticleSchedule.create(article, request);
            } else {
                // id o -> 기존 일정 수정
                articleSchedule = getExistingScheduleById(existingSchedules, request.scheduleId()).update(request);
            }
            articleScheduleJpaRepository.save(ArticleScheduleJpaEntity.from(articleSchedule));
        }

        return ArticleScheduleUpdateResponse.from(articleJpaEntity.getId());
    }

    @Transactional
    public ArticleScheduleDeleteResponse deleteArticleSchedules(Long userId, Long articleId) {
        // userId로 User 가져오기 - tokenId로 체크
        User user = userJpaRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND,
                HttpStatus.NOT_FOUND)).toModel();

        // 유효하지 않은 초기 여행 계획(article_id) 을 삭제하려고 할 경우
        ArticleJpaEntity articleJpaEntity = articleJpaRepository.findByIdAndStatusIn(articleId,
                List.of(
                    ArticleStatus.ACTIVE, ArticleStatus.PRIVATE))
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.ARTICLE_SCHEDULE_DELETE_INVALID,
                    HttpStatus.NOT_FOUND));

        // 유저가 작성한 초기 여행 계획(article_id)이 아닌 경우
        Article article = articleJpaEntity.toModel();
        article.verifyOwner(user);

        // 관련된 일정들
        List<ArticleSchedule> articleSchedules = articleScheduleJpaRepository.findByArticleJpaEntityAndStatusOrderByIdDesc(
            articleJpaEntity, ArticleScheduleStatus.ACTIVE).stream()
            .map(ArticleScheduleJpaEntity::toModel)
            .toList();

        // 초기 여행 계획 삭제
        article = article.delete();
        articleJpaRepository.save(ArticleJpaEntity.from(article));

        // 관련된 일정들 삭제
        for (ArticleSchedule articleSchedule : articleSchedules) {
            articleSchedule = articleSchedule.delete();
            articleScheduleJpaRepository.save(ArticleScheduleJpaEntity.from(articleSchedule));
        }

        return ArticleScheduleDeleteResponse.from(true);
    }

    private ArticleSchedule getExistingScheduleById(List<ArticleSchedule> schedules, Long id) {
        return schedules.stream()
            .filter(schedule -> schedule.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new BeApplicationException(
                ErrorCodes.ARTICLE_SCHEDULE_UPDATE_SCHEDULE_INVALID, HttpStatus.NOT_FOUND));
    }
}
