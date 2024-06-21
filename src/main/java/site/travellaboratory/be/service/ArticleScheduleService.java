package site.travellaboratory.be.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.controller.articleschedule.dto.ArticleScheduleRequest;
import site.travellaboratory.be.controller.articleschedule.dto.ArticleScheduleSaveResponse;
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
    public ArticleScheduleSaveResponse saveSchedules(Long userId, Long articleId, List<ArticleScheduleRequest> requests) {
        // 유효하지 않은 여행 계획에 대한 상세 일정을 작성할 경우
        Article article = articleRepository.findByIdAndStatusIn(articleId, List.of(
                ArticleStatus.ACTIVE, ArticleStatus.PRIVATE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.ARTICLE_SCHEDULE_POST_INVALID,
                HttpStatus.NOT_FOUND));

        // 유저가 작성한 초기 여행 계획이 아닌 경우
        if (!article.getUser().getId().equals(userId)) {
            throw new BeApplicationException(ErrorCodes.ARTICLE_SCHEDULE_POST_NOT_USER,
                HttpStatus.FORBIDDEN);
        }

        // 상세 일정 리스트 - 저장
        System.out.println("------------- 일정 상세 start -------------");
        List<ArticleSchedule> saveSchedules = requests.stream()
            .map(request -> toArticleSchedule(article, request))
            .toList();

        articleScheduleRepository.saveAll(saveSchedules);
        return ArticleScheduleSaveResponse.from(article.getId());
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
                    ArticleScheduleStatus.ACTIVE,
                    request.scheduleEtc()
                );
            }
            // dtype 에 general, transport, etc 외에 다른 값이 포함되어 있는 경우
            default -> throw new BeApplicationException(ErrorCodes.ARTICLE_SCHEDULE_POST_NOT_DTYPE,
                HttpStatus.BAD_REQUEST);
        }
    }
}
