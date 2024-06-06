package site.travellaboratory.be.user.repository;

import java.sql.Timestamp;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.travellaboratory.be.user.repository.entity.UserEntity;

public interface UserAuthRepository extends JpaRepository<UserEntity, Long> {

    @NotNull Optional<UserEntity> findByUserNameAndDeleteAtOrderByIdDesc(@NotNull String userName, @Nullable Timestamp deleteAt);

    @NotNull Optional<UserEntity> findByNickName(@NotNull String nickName);

    @NotNull Optional<UserEntity> findByIdAndDeleteAtOrderByIdDesc(@NotNull Long userId, Timestamp deleteAt);

    @Modifying
    @Query("UPDATE UserEntity u SET u.refreshToken = :refreshToken WHERE u.id = :userId")
    void updateRefreshToken(@Param("userId") Long userId, @Param("refreshToken") String refreshToken);
}
