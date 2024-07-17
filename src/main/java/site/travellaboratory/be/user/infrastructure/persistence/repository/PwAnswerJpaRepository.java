package site.travellaboratory.be.user.infrastructure.persistence.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import site.travellaboratory.be.user.infrastructure.persistence.entity.PwAnswerEntity;
import site.travellaboratory.be.user.domain._pw.enums.PwAnswerStatus;

public interface PwAnswerJpaRepository extends JpaRepository<PwAnswerEntity, Long> {
    PwAnswerEntity findByUserIdAndStatus(final Long userId, final PwAnswerStatus status);

    Optional<PwAnswerEntity> findByUserIdAndPwQuestionIdAndAnswerAndStatus(final Long userId,
        final Long pwQuestionId, final String answer, final PwAnswerStatus status);
}
