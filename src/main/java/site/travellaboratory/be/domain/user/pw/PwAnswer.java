package site.travellaboratory.be.domain.user.pw;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import site.travellaboratory.be.domain.user.pw.enums.PwAnswerStatus;

@Builder
@Getter
@RequiredArgsConstructor
public class PwAnswer {

    private final Long id;
    private final Long userId;
    private final Long pwQuestionId;
    private final String answer;
    private final PwAnswerStatus status;

    public static PwAnswer create(Long userId, Long pwQuestionId, String answer) {
        return PwAnswer.builder()
            .userId(userId)
            .pwQuestionId(pwQuestionId)
            .answer(answer)
            .status(PwAnswerStatus.ACTIVE)
            .build();
    }
}
