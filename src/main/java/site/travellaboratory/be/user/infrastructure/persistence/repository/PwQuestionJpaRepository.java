package site.travellaboratory.be.user.infrastructure.persistence.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import site.travellaboratory.be.user.infrastructure.persistence.entity.PwQuestionEntity;
import site.travellaboratory.be.user.domain._pw.enums.PwQuestionStatus;

public interface PwQuestionJpaRepository extends JpaRepository<PwQuestionEntity, Long> {

    Optional<PwQuestionEntity> findByIdAndStatus(Long pwQuestionId, PwQuestionStatus status);
}
