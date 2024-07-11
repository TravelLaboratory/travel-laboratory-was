package site.travellaboratory.be.infrastructure.domains.auth.pwquestion;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import site.travellaboratory.be.infrastructure.domains.auth.pwquestion.entity.PwQuestion;
import site.travellaboratory.be.infrastructure.domains.auth.pwquestion.enums.PwQuestionStatus;

public interface PwQuestionRepository extends JpaRepository<PwQuestion, Long> {

    Optional<PwQuestion> findByIdAndStatus(Long pwQuestionId, PwQuestionStatus status);
}
