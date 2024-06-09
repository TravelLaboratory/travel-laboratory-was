package site.travellaboratory.be.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    default User getById(final Long id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException("해당 유저는 없습니다."));
    }
}
