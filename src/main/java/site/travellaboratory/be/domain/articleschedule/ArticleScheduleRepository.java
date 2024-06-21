package site.travellaboratory.be.domain.articleschedule;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.travellaboratory.be.domain.article.Article;

public interface ArticleScheduleRepository extends JpaRepository<ArticleSchedule, Long> {

    // todo : N+1 before 전과
    // 상세 일정 리스트 조회 (Article 기준, 삭제된 건 제외, sortOrder 오름차순 정렬)
    List<ArticleSchedule> findByArticleAndStatusOrderBySortOrderAsc(Article article, ArticleScheduleStatus status);

    // todo: N+1 after 후 비교 해보기
    // 상세 일정 리스트 조회 (Article 기준, 삭제된 건 제외, sortOrder 오름차순 정렬) [N+1 해결]
    @Query("SELECT t FROM ArticleSchedule t " +
        "LEFT JOIN FETCH ScheduleGeneral sg ON sg.id = t.id " +
        "LEFT JOIN FETCH ScheduleTransport st ON st.id = t.id " +
        "LEFT JOIN FETCH ScheduleEtc se ON se.id = t.id " +
        "WHERE t.article = :article AND t.status = :status " +
        "ORDER BY t.sortOrder ASC")
    List<ArticleSchedule> findByArticleAndStatusOrderBySortOrderAscFetchJoinSchedules(@Param("article") Article article, @Param("status") ArticleScheduleStatus status);


    List<ArticleSchedule> findByArticleAndStatusOrderByIdDesc(Article article, ArticleScheduleStatus status);
}
