package site.travellaboratory.be.domain.auth.pwquestion;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import site.travellaboratory.be.domain.auth.pwquestion.enums.PwQuestionStatus;

public interface PwQuestionRepository extends JpaRepository<PwQuestion, Long> {

    Optional<PwQuestion> findByIdAndStatus(Long pwQuestionId, PwQuestionStatus status);
}
