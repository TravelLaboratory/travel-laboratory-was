package site.travellaboratory.be.application.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.infrastructure.domains.user.UserJpaRepository;
import site.travellaboratory.be.infrastructure.domains.user.entity.UserEntity;
import site.travellaboratory.be.domain.user.enums.UserStatus;
import site.travellaboratory.be.presentation.user.dto.reader.UserProfileResponse;

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
