package site.travellaboratory.be.tmp.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

//@MappedSuperclass
//@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hello") // 삭제할 것 처음 테스트용도
@Builder
public class TmpEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
