package site.travellaboratory.be.user.application.port;

import java.util.Optional;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain.enums.UserStatus;

public interface UserRepository {
    User findByUserId(Long userId);
    User getByIdAndStatus(Long userId, UserStatus status);
    Optional<User> findByUsernameAndStatusOrderByIdDesc(String username, UserStatus status);
    Optional<User> findByNickname(String nickname);
    User save(User user);
}
