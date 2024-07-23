package site.travellaboratory.be.user.application.port;

import java.util.Optional;
import site.travellaboratory.be.user.domain._pw.PwQuestion;
import site.travellaboratory.be.user.domain._pw.enums.PwQuestionStatus;

public interface PwQuestionRepository {
    Optional<PwQuestion> findByIdAndStatus(Long pwQuestionId, PwQuestionStatus status);

    PwQuestion save(PwQuestion pwQuestion);
}
