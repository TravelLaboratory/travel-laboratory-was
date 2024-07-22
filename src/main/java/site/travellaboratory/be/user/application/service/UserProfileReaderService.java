package site.travellaboratory.be.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.user.infrastructure.persistence.repository.UserJpaRepository;
import site.travellaboratory.be.user.infrastructure.persistence.entity.UserEntity;
import site.travellaboratory.be.user.domain.enums.UserStatus;
import site.travellaboratory.be.user.presentation.response.reader.UserProfileResponse;

@Service
@RequiredArgsConstructor
@Transactional
public class UserProfileReaderService {

    private final UserJpaRepository userJpaRepository;

    public UserProfileResponse findByUserProfile(final Long userId, final Long id) {
        final UserEntity userEntity = userJpaRepository.findByIdAndStatus(id, UserStatus.ACTIVE)
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.AUTH_USER_NOT_FOUND,
                        HttpStatus.BAD_REQUEST)
                );

        final boolean isEditable = userEntity.getId().equals(userId);

        return UserProfileResponse.from(userEntity, isEditable);
    }
}
