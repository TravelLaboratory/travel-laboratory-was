package site.travellaboratory.be.user.repository;

import java.sql.Timestamp;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.travellaboratory.be.user.repository.entity.UserEntity;

public interface UserAuthRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUserNameAndDeleteAtOrderByIdDesc(String userName, Timestamp deleteAt);
    Optional<UserEntity> findByNickName(String nickName);
    Optional<UserEntity> findByIdAndDeleteAtOrderByIdDesc(Long userId, Timestamp deleteAt);
    Optional<UserEntity> findByIdAndRefreshTokenAndDeleteAtOrderByIdDesc(Long userId, String refreshToken, Timestamp deleteAt);
    @Modifying
    @Query("UPDATE UserEntity u SET u.refreshToken = :refreshToken WHERE u.id = :userId")
    void updateRefreshToken(@Param("userId") Long userId, @Param("refreshToken") String refreshToken);
}
