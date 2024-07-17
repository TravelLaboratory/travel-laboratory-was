package site.travellaboratory.be.user.domain._pw;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import site.travellaboratory.be.user.domain._pw.enums.PwQuestionStatus;

@Builder
@Getter
@RequiredArgsConstructor
public class PwQuestion {

    private final Long id;
    private final String question;
    private final PwQuestionStatus status;
}
