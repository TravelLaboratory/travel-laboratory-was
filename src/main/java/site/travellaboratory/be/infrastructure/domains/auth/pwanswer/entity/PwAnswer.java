package site.travellaboratory.be.infrastructure.domains.auth.pwanswer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.travellaboratory.be.infrastructure.common.BaseEntity;
import site.travellaboratory.be.infrastructure.domains.auth.pwanswer.enums.PwAnswerStatus;
import site.travellaboratory.be.infrastructure.domains.auth.pwquestion.entity.PwQuestion;
import site.travellaboratory.be.infrastructure.domains.user.entity.User;

@Entity
@Getter
@NoArgsConstructor
public class PwAnswer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "pw_question_id")
    private PwQuestion pwQuestion;

    @Column(nullable = false)
    private String answer;

    @Enumerated(EnumType.STRING)
    private PwAnswerStatus status;

    private PwAnswer(final User user, final PwQuestion pwQuestion, final String answer) {
        this.user = user;
        this.pwQuestion = pwQuestion;
        this.answer = answer;
    }

    public static PwAnswer of(final User user, final PwQuestion pwQuestion, final String answer) {
        return new PwAnswer(user, pwQuestion, answer);
    }

    @PrePersist
    void prePersist() {
        this.status = PwAnswerStatus.ACTIVE;
    }
}
