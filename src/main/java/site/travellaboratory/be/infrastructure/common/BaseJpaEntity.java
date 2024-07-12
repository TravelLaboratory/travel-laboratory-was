package site.travellaboratory.be.infrastructure.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@NoArgsConstructor
public abstract class BaseJpaEntity {

    @CreatedDate // 처음 저장될 때 자동으로 기록
    @Column(updatable = false, nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime createdAt;

    @LastModifiedDate // 수정될 때 자동으로 기록
    @Column(nullable = false, columnDefinition = "DATETIME") // create 로 할 경우 datetime(6)으로 소수점까지 들어감
    private LocalDateTime updatedAt;

}
