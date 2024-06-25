package site.travellaboratory.be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.controller.auth.dto.UserLoginResponse;
import site.travellaboratory.be.controller.jwt.dto.AuthTokenResponse;
import site.travellaboratory.be.controller.jwt.util.AuthTokenGenerator;
import site.travellaboratory.be.controller.oauth.dto.OAuthJoinRequest;
import site.travellaboratory.be.domain.user.UserRepository;
import site.travellaboratory.be.domain.user.entity.User;
import site.travellaboratory.be.domain.user.entity.UserStatus;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final UserRepository userRepository;
    private final AuthTokenGenerator authTokenGenerator;

    public UserLoginResponse login(final OAuthJoinRequest oAuthJoinRequest) {
        User user = userRepository.findByUsernameAndStatusOrderByIdDesc(
                oAuthJoinRequest.accountEmail(),
                UserStatus.ACTIVE)
            .orElse(
                userRepository.save(
                    User.socialOf(oAuthJoinRequest.accountEmail(), oAuthJoinRequest.profileImage(),
                        oAuthJoinRequest.profileNickname(), oAuthJoinRequest.isAgreement()))
            );

        Long userId = user.getId();

        // 저장된 userId로 다시 조회
        // profile_img_url 전송
        // 여기서 orElseThrow 에 갈일은 위에서 회원가입 여부를 체크하기 없음
        User loginUser = userRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(
                ErrorCodes.AUTH_USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        AuthTokenResponse authTokenResponse = authTokenGenerator.generateTokens(userId);
        return UserLoginResponse.from(loginUser, authTokenResponse);
    }
}
