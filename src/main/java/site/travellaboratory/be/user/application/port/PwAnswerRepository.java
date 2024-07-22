package site.travellaboratory.be.user.application.port;

import java.util.Optional;
import site.travellaboratory.be.user.domain._pw.PwAnswer;
import site.travellaboratory.be.user.domain._pw.enums.PwAnswerStatus;

public interface PwAnswerRepository {
    PwAnswer getByUserIdAndStatus(final Long userId, final PwAnswerStatus status);
    Optional<PwAnswer> findByUserIdAndPwQuestionIdAndAnswerAndStatus(final Long userId, final Long pwQuestionId, final String answer, final PwAnswerStatus status);
    PwAnswer save(PwAnswer pwAnswer);

}
