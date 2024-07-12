package site.travellaboratory.be.infrastructure.domains.auth.pwanswer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.travellaboratory.be.infrastructure.common.BaseEntity;
import site.travellaboratory.be.infrastructure.domains.auth.pwanswer.enums.PwAnswerStatus;
import site.travellaboratory.be.infrastructure.domains.auth.pwquestion.entity.PwQuestion;

@Entity
@Getter
@NoArgsConstructor
public class PwAnswer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "pw_question_id")
    private PwQuestion pwQuestion;

    @Column(nullable = false)
    private String answer;

    @Enumerated(EnumType.STRING)
    private PwAnswerStatus status;

    private PwAnswer(Long userId, PwQuestion pwQuestion, String answer) {
        this.userId = userId;
        this.pwQuestion = pwQuestion;
        this.answer = answer;
    }

    public static PwAnswer of(Long userId, PwQuestion pwQuestion, String answer) {
        return new PwAnswer(userId, pwQuestion, answer);
    }

    @PrePersist
    void prePersist() {
        this.status = PwAnswerStatus.ACTIVE;
    }
}
