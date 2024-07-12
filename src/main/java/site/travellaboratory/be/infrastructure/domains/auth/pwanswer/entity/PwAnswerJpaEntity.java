package site.travellaboratory.be.infrastructure.domains.auth.pwanswer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.travellaboratory.be.domain.user.pw.PwAnswer;
import site.travellaboratory.be.domain.user.pw.enums.PwAnswerStatus;
import site.travellaboratory.be.infrastructure.common.BaseEntity;

@Entity
@Getter
@NoArgsConstructor
public class PwAnswerJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    private Long userId;

    @JoinColumn(name = "pw_question_id")
    private Long pwQuestionId;

    @Column(nullable = false)
    private String answer;

    @Enumerated(EnumType.STRING)
    private PwAnswerStatus status;

    public static PwAnswerJpaEntity from(PwAnswer pwAnswer) {
        PwAnswerJpaEntity result = new PwAnswerJpaEntity();
        result.id = pwAnswer.getId();
        result.userId = pwAnswer.getUserId();
        result.pwQuestionId = pwAnswer.getPwQuestionId();
        result.answer = pwAnswer.getAnswer();
        result.status = pwAnswer.getStatus();
        return result;
    }

    public PwAnswer toModel() {
        return PwAnswer.builder()
            .id(this.id)
            .userId(this.userId)
            .pwQuestionId(this.pwQuestionId)
            .answer(this.answer)
            .status(this.status)
            .build();
    }
}
