package site.travellaboratory.be.infrastructure.domains.articleschedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.travellaboratory.be.infrastructure.domains.articleschedule.entity.ScheduleGeneralJpaEntity;

public interface ScheduleGeneralJpaRepository extends JpaRepository<ScheduleGeneralJpaEntity, Long> {

}
