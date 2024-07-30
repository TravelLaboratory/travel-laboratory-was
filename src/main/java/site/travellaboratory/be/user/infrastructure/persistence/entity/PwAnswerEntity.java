package site.travellaboratory.be.user.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.travellaboratory.be.user.domain._pw.PwAnswer;
import site.travellaboratory.be.user.domain._pw.enums.PwAnswerStatus;
import site.travellaboratory.be.common.infrastructure.common.BaseEntity;

@Entity
@Table(name = "pw_answer")
@Getter
@NoArgsConstructor
public class PwAnswerEntity extends BaseEntity {

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

    public static PwAnswerEntity from(PwAnswer pwAnswer) {
        PwAnswerEntity result = new PwAnswerEntity();
        result.id = pwAnswer.getId();
        result.userId = pwAnswer.getUserId();
        result.pwQuestionId = pwAnswer.getPwQuestionId();
        result.answer = pwAnswer.getAnswer();
        result.status = pwAnswer.getStatus();
        result.setCreatedAt(pwAnswer.getCreatedAt());
        result.setUpdatedAt(pwAnswer.getUpdatedAt());
        return result;
    }

    public PwAnswer toModel() {
        return PwAnswer.builder()
            .id(this.id)
            .userId(this.userId)
            .pwQuestionId(this.pwQuestionId)
            .answer(this.answer)
            .status(this.status)
            .createdAt(this.getCreatedAt())
            .updatedAt(this.getUpdatedAt())
            .build();
    }
}
