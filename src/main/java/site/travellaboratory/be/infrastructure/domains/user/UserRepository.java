package site.travellaboratory.be.infrastructure.domains.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.travellaboratory.be.domain.user.enums.UserStatus;
import site.travellaboratory.be.infrastructure.domains.user.entity.UserJpaEntity;

@Repository
public interface UserRepository extends JpaRepository<UserJpaEntity, Long> {

    // todo: 수정 요망
//    default User getById(final Long id) {
//        return findById(id).orElseThrow(() -> new IllegalArgumentException("해당 유저는 없습니다."));
//    }

    // todo : 이걸로
    Optional<UserJpaEntity> findByIdAndStatus(Long userId, UserStatus status);

    Optional<UserJpaEntity> findByUsernameAndStatusOrderByIdDesc(String username, UserStatus status);
    Optional<UserJpaEntity> findByNickname(String nickname);
}
