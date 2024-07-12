package site.travellaboratory.be.infrastructure.domains.auth.pwquestion;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import site.travellaboratory.be.infrastructure.domains.auth.pwquestion.entity.PwQuestionJpaEntity;
import site.travellaboratory.be.domain.user.pw.enums.PwQuestionStatus;

public interface PwQuestionRepository extends JpaRepository<PwQuestionJpaEntity, Long> {

    Optional<PwQuestionJpaEntity> findByIdAndStatus(Long pwQuestionId, PwQuestionStatus status);
}
