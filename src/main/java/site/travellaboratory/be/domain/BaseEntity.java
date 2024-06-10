package site.travellaboratory.be.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class BaseEntity {

    @Column(updatable = false, nullable = false)
    private LocalDateTime registerAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public BaseEntity() {
        this.registerAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void update() {
        this.updatedAt = LocalDateTime.now();
    }
}
