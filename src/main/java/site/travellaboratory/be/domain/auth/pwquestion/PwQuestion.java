package site.travellaboratory.be.domain.auth.pwquestion;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import site.travellaboratory.be.domain.BaseEntity;
import site.travellaboratory.be.domain.auth.pwquestion.enums.PwQuestionStatus;

@Entity
public class PwQuestion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String question;

    @Enumerated(EnumType.STRING)
    private PwQuestionStatus status;

    @PrePersist
    void prePersist() {
        this.status = PwQuestionStatus.ACTIVE;
    }
}
