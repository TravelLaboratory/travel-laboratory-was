package site.travellaboratory.be.jwt.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.jwt.dto.AccessTokenResponse;
import site.travellaboratory.be.jwt.dto.AuthTokenResponse;

@Component
@RequiredArgsConstructor
public class AuthTokenGenerator {

    private final JwtTokenUtility jwtTokenUtility;

    @Value("${jwt.access-token.plus-hour}")
    private Long accessTokenPlusHour;

    @Value("${jwt.refresh-token.plus-hour}")
    private Long refreshTokenPlusHour;

    public AuthTokenResponse generateTokens(Long userId) {
        String accessToken = jwtTokenUtility.issueToken(userId, accessTokenPlusHour);
        String refreshToken = jwtTokenUtility.issueToken(userId, refreshTokenPlusHour);
        return AuthTokenResponse.from(accessToken, refreshToken);
    }

    public AccessTokenResponse reIssueAccessToken(Long userId) {
        String reIssueAccessToken = jwtTokenUtility.issueToken(userId, accessTokenPlusHour);
        return AccessTokenResponse.from(reIssueAccessToken);
    }

    public Long getRefreshTokenUserId(String accessToken, String refreshToken) {
        // 토큰 만료 유무 체크
        if (!(jwtTokenUtility.isExpired(accessToken))) {
            throw new BeApplicationException(ErrorCodes.REFRESH_TOKEN_NOT_EXPIRED_ACCESS_TOKEN,
                HttpStatus.BAD_REQUEST);
        }
        // 리프레시 토큰 검증
        return jwtTokenUtility.extractUserId(refreshToken);
    }
}
