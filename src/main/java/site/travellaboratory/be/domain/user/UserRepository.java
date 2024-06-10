package site.travellaboratory.be.domain.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.travellaboratory.be.domain.user.entity.User;
import site.travellaboratory.be.domain.user.entity.UserStatus;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    default User getById(final Long id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException("해당 유저는 없습니다."));
    }

    Optional<User> findByUsernameAndStatusOrderByIdDesc(String username, UserStatus status);
    Optional<User> findByNickname(String nickname);
}
