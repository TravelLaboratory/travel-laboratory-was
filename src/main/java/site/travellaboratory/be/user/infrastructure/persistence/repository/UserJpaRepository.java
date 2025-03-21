package site.travellaboratory.be.user.infrastructure.persistence.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.travellaboratory.be.user.domain.enums.UserStatus;
import site.travellaboratory.be.user.infrastructure.persistence.entity.UserEntity;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByIdAndStatus(Long userId, UserStatus status);
    Optional<UserEntity> findByUsernameAndStatusOrderByIdDesc(String username, UserStatus status);
    Optional<UserEntity> findByNickname(String nickname);
}
