package site.travellaboratory.be.domain.article;

import java.sql.Time;
import java.time.LocalDate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import site.travellaboratory.be.domain.article.enums.ArticleScheduleStatus;

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



//    public void delete() {
//        this.status = ArticleScheduleStatus.INACTIVE;
//    }
//
//    public void update(ArticleScheduleRequest request) {
//        this.visitedDate = request.visitedDate();
//        this.visitedTime = request.visitedTime();
//        this.sortOrder = request.sortOrder();
//        this.category = request.category();
//        this.durationTime = request.durationTime();
//        this.expense = request.expense();
//        this.memo = request.memo();
//    }
}
