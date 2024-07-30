package site.travellaboratory.be.user.domain._pw;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import site.travellaboratory.be.user.domain._pw.enums.PwAnswerStatus;

@Builder
@Getter
@RequiredArgsConstructor
public class PwAnswer {

    private final Long id;
    private final Long userId;
    private final Long pwQuestionId;
    private final String answer;
    private final PwAnswerStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static PwAnswer create(Long userId, Long pwQuestionId, String answer) {
        return PwAnswer.builder()
            .userId(userId)
            .pwQuestionId(pwQuestionId)
            .answer(answer)
            .status(PwAnswerStatus.ACTIVE)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }
}
