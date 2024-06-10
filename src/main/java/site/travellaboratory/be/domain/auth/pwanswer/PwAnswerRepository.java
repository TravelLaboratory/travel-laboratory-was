package site.travellaboratory.be.domain.auth.pwanswer;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import site.travellaboratory.be.domain.auth.pwanswer.enums.PwAnswerStatus;

public interface PwAnswerRepository extends JpaRepository<PwAnswer, Long> {
    PwAnswer findByUserIdAndStatus(final Long userId, final PwAnswerStatus status);

    Optional<PwAnswer> findByUserIdAndPwQuestionIdAndAnswerAndStatus(final Long userId,
        final Long pwQuestionId, final String answer, final PwAnswerStatus status);
}
