package site.travellaboratory.be.infrastructure.domains.articleschedule.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import site.travellaboratory.be.infrastructure.domains.articleschedule.entity.ScheduleTransportJpaEntity;

public interface ScheduleTransportJpaRepository extends JpaRepository<ScheduleTransportJpaEntity, Long> {

}
