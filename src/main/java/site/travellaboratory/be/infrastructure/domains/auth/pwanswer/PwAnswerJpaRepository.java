package site.travellaboratory.be.infrastructure.domains.auth.pwanswer;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import site.travellaboratory.be.infrastructure.domains.auth.pwanswer.entity.PwAnswerEntity;
import site.travellaboratory.be.domain.user.pw.enums.PwAnswerStatus;

public interface PwAnswerJpaRepository extends JpaRepository<PwAnswerEntity, Long> {
    PwAnswerEntity findByUserIdAndStatus(final Long userId, final PwAnswerStatus status);

    Optional<PwAnswerEntity> findByUserIdAndPwQuestionIdAndAnswerAndStatus(final Long userId,
        final Long pwQuestionId, final String answer, final PwAnswerStatus status);
}
