package site.travellaboratory.be.user.infrastructure.persistence.repository;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import site.travellaboratory.be.user.application.port.PwQuestionRepository;
import site.travellaboratory.be.user.domain._pw.PwQuestion;
import site.travellaboratory.be.user.domain._pw.enums.PwQuestionStatus;
import site.travellaboratory.be.user.infrastructure.persistence.entity.PwQuestionEntity;

@Repository
@RequiredArgsConstructor
public class PwQuestionRepositoryImpl implements PwQuestionRepository {

    private final PwQuestionJpaRepository pwQuestionJpaRepository;

    @Override
    public Optional<PwQuestion> findByIdAndStatus(Long pwQuestionId, PwQuestionStatus status) {
        return pwQuestionJpaRepository.findByIdAndStatus(pwQuestionId, status).map(PwQuestionEntity::toModel);
    }

    @Override
    public PwQuestion save(PwQuestion pwQuestion) {
        return pwQuestionJpaRepository.save(PwQuestionEntity.from(pwQuestion)).toModel();
    }
}
