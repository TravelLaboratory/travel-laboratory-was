package site.travellaboratory.be.infrastructure.domains.articleschedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.travellaboratory.be.infrastructure.domains.articleschedule.entity.ScheduleGeneralEntity;

public interface ScheduleGeneralJpaRepository extends JpaRepository<ScheduleGeneralEntity, Long> {

}
