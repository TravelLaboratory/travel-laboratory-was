package site.travellaboratory.be.infrastructure.domains.auth.pwquestion.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.travellaboratory.be.domain.user.pw.PwQuestion;
import site.travellaboratory.be.infrastructure.common.BaseEntity;
import site.travellaboratory.be.domain.user.pw.enums.PwQuestionStatus;

@Entity
@Getter
@NoArgsConstructor
public class PwQuestionJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String question;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PwQuestionStatus status;

    @PrePersist
    void prePersist() {
        this.status = PwQuestionStatus.ACTIVE;
    }

    public static PwQuestionJpaEntity from(PwQuestion pwQuestion) {
        PwQuestionJpaEntity result = new PwQuestionJpaEntity();
        result.question = pwQuestion.getQuestion();
        return result;
    }

    public PwQuestion toModel() {
        return PwQuestion.builder()
            .id(this.id)
            .question(this.question)
            .status(this.status)
            .build();
    }
}
