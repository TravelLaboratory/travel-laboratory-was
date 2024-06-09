package site.travellaboratory.be.user.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import site.travellaboratory.be.user.repository.entity.UserEntity;
import site.travellaboratory.be.user.repository.entity.UserStatus;

public interface UserAuthRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUserNameAndStatusOrderByIdDesc(String userName, UserStatus status);
    Optional<UserEntity> findByNickName(String nickName);
}