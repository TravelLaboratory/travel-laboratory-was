package site.travellaboratory.be.user.infrastructure.jwt.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.travellaboratory.be.user.application.port.TokenManager;
import site.travellaboratory.be.user.domain._auth.Tokens;
import site.travellaboratory.be.user.domain._auth.Token;
import site.travellaboratory.be.user.infrastructure.jwt.manager.helper.JwtTokenGenerator;
import site.travellaboratory.be.user.infrastructure.jwt.manager.helper.JwtTokenParser;

@Component
@RequiredArgsConstructor
public class JwtTokenManager implements TokenManager {

    private final JwtTokenGenerator jwtTokenGenerator;
    private final JwtTokenParser jwtTokenParser;

    @Override
    public Tokens generateTokens(final Long userId) {
        Token accessToken = jwtTokenGenerator.issueAccessToken(userId);
        Token refreshToken = jwtTokenGenerator.issueRefreshToken(userId);

        return Tokens.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

    @Override
    public Token reIssueAccessToken(final String accessToken, final String refreshToken) {
        Long userId = jwtTokenParser.getRefreshTokenUserId(accessToken, refreshToken);
        return jwtTokenGenerator.issueAccessToken(userId);
    }
}
