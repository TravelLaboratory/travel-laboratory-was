package site.travellaboratory.be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.travellaboratory.be.controller.jwt.dto.AuthTokenResponse;
import site.travellaboratory.be.controller.jwt.util.AuthTokenGenerator;
import site.travellaboratory.be.controller.oauth.dto.OAuthJoinRequest;
import site.travellaboratory.be.controller.oauth.dto.OAuthTokenResponse;
import site.travellaboratory.be.domain.user.UserRepository;
import site.travellaboratory.be.domain.user.entity.User;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final UserRepository userRepository;
    private final AuthTokenGenerator authTokenGenerator;

    public AuthTokenResponse login(final OAuthJoinRequest oAuthJoinRequest) {
        User user = userRepository.save(User.socialOf(oAuthJoinRequest.accountEmail(), oAuthJoinRequest.profileImage(),
                oAuthJoinRequest.profileNickname(), oAuthJoinRequest.isAgreement()));

        Long userId = user.getId();
        return authTokenGenerator.generateTokens(userId);
    }
}
