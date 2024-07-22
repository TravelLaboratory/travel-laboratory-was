package site.travellaboratory.be.user.infrastructure.jwt.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.travellaboratory.be.user.infrastructure.jwt.manager.helper.JwtTokenParser;
import site.travellaboratory.be.user.infrastructure.jwt.manager.helper.JwtTokenValidator;

@Component
@RequiredArgsConstructor
public class JwtTokenVerifier {

    private final JwtTokenValidator jwtTokenValidator;
    private final JwtTokenParser jwtTokenParser;

    public Long getAccessTokenUserId(final String accessToken) {
        // token 검사
        jwtTokenValidator.validAccessTokenWithThrow(accessToken);
        return jwtTokenParser.getUserIdBy(accessToken);
    }
}
