package site.travellaboratory.be.article.infrastructure.persistence.repository._schedule;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import site.travellaboratory.be.article.infrastructure.persistence.entity.ArticleEntity;
import site.travellaboratory.be.article.infrastructure.persistence.entity._schedule.ArticleScheduleEntity;
import site.travellaboratory.be.article.domain._schedule.enums.ArticleScheduleStatus;

public interface ArticleScheduleJpaRepository extends JpaRepository<ArticleScheduleEntity, Long> {

    // 상세 일정 리스트 조회 (Article 기준, 삭제된 건 제외, sortOrder 오름차순 정렬)
    List<ArticleScheduleEntity> findByArticleEntityAndStatusOrderBySortOrderAsc(
        ArticleEntity articleEntity, ArticleScheduleStatus status);

    // 일정 상세 리스트 - 삭제를 위한 비교 데이터
    List<ArticleScheduleEntity> findByArticleEntityAndStatusOrderByIdDesc(
        ArticleEntity articleEntity, ArticleScheduleStatus status);
}
