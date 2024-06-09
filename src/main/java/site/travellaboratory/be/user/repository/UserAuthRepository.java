package site.travellaboratory.be.user.repository;

import java.sql.Timestamp;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import site.travellaboratory.be.user.repository.entity.UserEntity;

public interface UserAuthRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUserNameAndDeleteAtOrderByIdDesc(String userName, Timestamp deleteAt);
    Optional<UserEntity> findByNickName(String nickName);
    Optional<UserEntity> findByIdAndDeleteAtOrderByIdDesc(Long userId, Timestamp deleteAt);
}