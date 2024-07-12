package site.travellaboratory.be.application.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.presentation.auth.dto.userauthentication.UserLoginResponse;
import site.travellaboratory.be.presentation.auth.dto.userauthentication.AuthTokenResponse;
import site.travellaboratory.be.infrastructure.domains.auth.jwt.helper.AuthTokenGenerator;
import site.travellaboratory.be.presentation.auth.dto.oauth.OAuthJoinRequest;
import site.travellaboratory.be.infrastructure.domains.user.UserJpaRepository;
import site.travellaboratory.be.infrastructure.domains.user.entity.UserJpaEntity;
import site.travellaboratory.be.domain.user.enums.UserStatus;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final UserJpaRepository userJpaRepository;
    private final AuthTokenGenerator authTokenGenerator;

    public UserLoginResponse login(final OAuthJoinRequest oAuthJoinRequest) {
        UserJpaEntity userJpaEntity = userJpaRepository.findByUsernameAndStatusOrderByIdDesc(
                oAuthJoinRequest.accountEmail(),
                UserStatus.ACTIVE)
            .orElseGet(() -> {
                return userJpaRepository.save(
                    UserJpaEntity.socialOf(oAuthJoinRequest.accountEmail(), oAuthJoinRequest.profileImage(),
                        oAuthJoinRequest.profileNickname(), oAuthJoinRequest.isAgreement()));
                });

        Long userId = userJpaEntity.getId();

        // 저장된 userId로 다시 조회
        // profile_img_url 전송
        // 여기서 orElseThrow 에 갈일은 위에서 회원가입 여부를 체크하기 없음
        UserJpaEntity loginUserJpaEntity = userJpaRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(
                ErrorCodes.AUTH_USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        AuthTokenResponse authTokenResponse = authTokenGenerator.generateTokens(userId);
        return UserLoginResponse.from(loginUserJpaEntity, authTokenResponse);
    }
}
