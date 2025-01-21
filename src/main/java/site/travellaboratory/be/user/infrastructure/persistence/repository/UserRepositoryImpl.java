package site.travellaboratory.be.user.infrastructure.persistence.repository;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import site.travellaboratory.be.common.presentation.exception.BeApplicationException;
import site.travellaboratory.be.common.presentation.error.ErrorCodes;
import site.travellaboratory.be.user.application.port.UserRepository;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain.enums.UserStatus;
import site.travellaboratory.be.user.infrastructure.persistence.entity.UserEntity;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User findByUserId(Long userId) {
        return userJpaRepository.findById(userId).orElseThrow(
                () -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND, HttpStatus.NOT_FOUND))
            .toModel();
    }

    @Override
    public User getByIdAndStatus(Long userId, UserStatus status) {
        return userJpaRepository.findByIdAndStatus(userId, status)
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND, HttpStatus.NOT_FOUND))
            .toModel();
    }

    @Override
    public Optional<User> findByUsernameAndStatusOrderByIdDesc(String username, UserStatus status) {
        return userJpaRepository.findByUsernameAndStatusOrderByIdDesc(username, status).map(UserEntity::toModel);
    }

    @Override
    public Optional<User> findByNickname(String nickname) {
        return userJpaRepository.findByNickname(nickname).map(UserEntity::toModel);
    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(UserEntity.from(user)).toModel();
    }
}
