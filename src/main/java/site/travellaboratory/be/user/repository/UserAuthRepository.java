package site.travellaboratory.be.user.repository;

import java.sql.Timestamp;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import site.travellaboratory.be.user.repository.entity.UserEntity;

public interface UserAuthRepository extends JpaRepository<UserEntity, Long> {

    @NotNull Optional<UserEntity> findByUserName(@NotNull String userName);

    @NotNull Optional<UserEntity> findByNickName(@NotNull String nickName);

    @NotNull Optional<UserEntity> findByIdAndDeleteAtOrderByIdDesc(@NotNull Long userId, Timestamp deleteAt);

}
