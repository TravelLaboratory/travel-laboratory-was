package site.travellaboratory.be.user.infrastructure.persistence.repository;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import site.travellaboratory.be.user.application.port.PwAnswerRepository;
import site.travellaboratory.be.user.domain._pw.PwAnswer;
import site.travellaboratory.be.user.domain._pw.enums.PwAnswerStatus;
import site.travellaboratory.be.user.infrastructure.persistence.entity.PwAnswerEntity;

@Repository
@RequiredArgsConstructor
public class PwAnswerRepositoryImpl implements PwAnswerRepository {

    private final PwAnswerJpaRepository pwAnswerJpaRepository;

    @Override
    public PwAnswer getByUserIdAndStatus(Long userId, PwAnswerStatus status) {
        return pwAnswerJpaRepository.findByUserIdAndStatus(userId, status).toModel();
    }

    @Override
    public Optional<PwAnswer> findByUserIdAndPwQuestionIdAndAnswerAndStatus(Long userId, Long pwQuestionId, String answer, PwAnswerStatus status) {
        return pwAnswerJpaRepository.findByUserIdAndPwQuestionIdAndAnswerAndStatus(userId, pwQuestionId, answer, status).map(PwAnswerEntity::toModel);
    }

    @Override
    public PwAnswer save(PwAnswer pwAnswer) {
        return pwAnswerJpaRepository.save(PwAnswerEntity.from(pwAnswer)).toModel();
    }
}
