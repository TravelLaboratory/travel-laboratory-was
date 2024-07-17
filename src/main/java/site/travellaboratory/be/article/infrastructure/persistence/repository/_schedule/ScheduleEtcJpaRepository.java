package site.travellaboratory.be.article.infrastructure.persistence.repository._schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import site.travellaboratory.be.article.infrastructure.persistence.entity._schedule.ScheduleEtcEntity;

public interface ScheduleEtcJpaRepository extends JpaRepository<ScheduleEtcEntity, Long> {

}
