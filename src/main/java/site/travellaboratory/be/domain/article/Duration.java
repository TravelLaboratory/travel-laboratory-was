package site.travellaboratory.be.domain.article;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
public class Duration {

    @Column(updatable = false, nullable = false)
    private LocalDateTime startAt;

    @Column(updatable = false, nullable = false)
    private LocalDateTime endAt;

    public Duration() {
    }

    public Duration(LocalDateTime startAt, LocalDateTime endAt) {
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }
}
