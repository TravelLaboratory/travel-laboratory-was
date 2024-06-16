package site.travellaboratory.be.domain.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.travellaboratory.be.domain.user.entity.User;
import site.travellaboratory.be.domain.user.entity.UserStatus;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // todo: 수정 요망
//    default User getById(final Long id) {
//        return findById(id).orElseThrow(() -> new IllegalArgumentException("해당 유저는 없습니다."));
//    }

    // todo : 이걸로
    Optional<User> findByIdAndStatus(Long userId, UserStatus status);

    Optional<User> findByUsernameAndStatusOrderByIdDesc(String username, UserStatus status);
    Optional<User> findByNickname(String nickname);
}
