package site.travellaboratory.be.infrastructure.domains.auth.pwquestion;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import site.travellaboratory.be.infrastructure.domains.auth.pwquestion.entity.PwQuestionEntity;
import site.travellaboratory.be.domain.user.pw.enums.PwQuestionStatus;

public interface PwQuestionJpaRepository extends JpaRepository<PwQuestionEntity, Long> {

    Optional<PwQuestionEntity> findByIdAndStatus(Long pwQuestionId, PwQuestionStatus status);
}
