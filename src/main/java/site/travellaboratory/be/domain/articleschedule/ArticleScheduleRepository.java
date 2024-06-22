package site.travellaboratory.be.domain.articleschedule;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import site.travellaboratory.be.domain.article.Article;

public interface ArticleScheduleRepository extends JpaRepository<ArticleSchedule, Long> {

    // 상세 일정 리스트 조회 (Article 기준, 삭제된 건 제외, sortOrder 오름차순 정렬)
    List<ArticleSchedule> findByArticleAndStatusOrderBySortOrderAsc(Article article, ArticleScheduleStatus status);

    List<ArticleSchedule> findByArticleAndStatusOrderByIdDesc(Article article, ArticleScheduleStatus status);
}
