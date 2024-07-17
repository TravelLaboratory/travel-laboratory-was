package site.travellaboratory.be.article.infrastructure.persistence.repository._schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import site.travellaboratory.be.article.infrastructure.persistence.entity._schedule.ScheduleGeneralEntity;

public interface ScheduleGeneralJpaRepository extends JpaRepository<ScheduleGeneralEntity, Long> {

}
