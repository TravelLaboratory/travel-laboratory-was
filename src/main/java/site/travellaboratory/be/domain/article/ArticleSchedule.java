package site.travellaboratory.be.domain.article;

import java.sql.Time;
import java.time.LocalDate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.domain.article.enums.ArticleScheduleStatus;
import site.travellaboratory.be.presentation.articleschedule.dto.writer.ArticleScheduleRequest;

@Getter
@RequiredArgsConstructor
public abstract class ArticleSchedule{

    private final Long id;
    private final Article article;
    private final LocalDate visitedDate;
    private final Time visitedTime;
    private final Integer sortOrder;
    private final String category;
    private final Time durationTime;
    private final String expense;
    private final String memo;
    private final ArticleScheduleStatus status;
    private final String dtype;

    public static ArticleSchedule create(Article article, ArticleScheduleRequest request) {
        switch (request.dtype()) {
            case "GENERAL" -> {
                return ScheduleGeneral.create(article, request);
            }
            // dtype - transport
            case "TRANSPORT" -> {
                return ScheduleTransport.create(article, request);
            }
            case "ETC" -> {
                return ScheduleEtc.create(article, request);
            }
            // dtype 에 general, transport, etc 외에 다른 값이 포함되어 있는 경우
            default -> throw new BeApplicationException(ErrorCodes.ARTICLE_SCHEDULE_POST_NOT_DTYPE,
                HttpStatus.BAD_REQUEST);
        }
    }

    public abstract ArticleSchedule update(ArticleScheduleRequest request);

    public abstract ArticleSchedule delete();

    // 유효하지 않은 일정을 수정하려는 경우 (삭제 혹은 임의의 scheduleId를 넣어서 요청한 경우)
    protected void verifyArticleSchedule(Long scheduleId) {
        if (!this.getId().equals(scheduleId)) {
            throw new BeApplicationException(ErrorCodes.ARTICLE_SCHEDULE_UPDATE_SCHEDULE_INVALID, HttpStatus.NOT_FOUND);
        }
    }
}
