package site.travellaboratory.be.domain.auth.pwanswer;

import org.springframework.data.jpa.repository.JpaRepository;
import site.travellaboratory.be.domain.auth.pwanswer.enums.PwAnswerStatus;

public interface PwAnswerRepository extends JpaRepository<PwAnswer, Long> {
    PwAnswer findByUserIdAndStatus(Long userId, PwAnswerStatus status);

}
