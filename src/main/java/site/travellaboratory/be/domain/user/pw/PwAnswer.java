package site.travellaboratory.be.domain.user.pw;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import site.travellaboratory.be.domain.user.pw.enums.PwAnswerStatus;
import site.travellaboratory.be.domain.user.user.User;

@Builder
@Getter
@RequiredArgsConstructor
public class PwAnswer {

    private final Long id;
    private final User user;
    private final PwQuestion pwQuestion;
    private final String answer;
    private final PwAnswerStatus status;

    public static PwAnswer create(User user, PwQuestion pwQuestion, String answer) {
        return PwAnswer.builder()
            .user(user)
            .pwQuestion(pwQuestion)
            .answer(answer)
            .status(PwAnswerStatus.ACTIVE)
            .build();
    }
}
