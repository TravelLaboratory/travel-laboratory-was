package site.travellaboratory.be.user.infrastructure.jwt.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.user.domain._auth.Token;
import site.travellaboratory.be.user.domain._auth.Tokens;
import site.travellaboratory.be.user.infrastructure.jwt.manager.helper.JwtTokenGenerator;
import site.travellaboratory.be.user.infrastructure.jwt.manager.helper.JwtTokenParser;

@ExtendWith(MockitoExtension.class)
class JwtTokenManagerTest {

    @InjectMocks
    private JwtTokenManager sut;

    @Mock
    private JwtTokenGenerator jwtTokenGenerator;

    @Mock
    private JwtTokenParser jwtTokenParser;

    @Nested
    class GenerateTokens {

        @DisplayName("성공 - 토큰 생성")
        @Test
        void test1000() {
            //given
            Long userId = 1L;
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime accessTokenExpiredAt = currentTime.plusHours(1);
            LocalDateTime refreshTokenExpiredAt = currentTime.plusHours(2);
            Token accessToken = new Token("access_token", accessTokenExpiredAt);
            Token refreshToken = new Token("refresh_token", refreshTokenExpiredAt);

            when(jwtTokenGenerator.issueAccessToken(eq(userId))).thenReturn(accessToken);
            when(jwtTokenGenerator.issueRefreshToken(eq(userId))).thenReturn(refreshToken);

            //when
            Tokens tokens = sut.generateTokens(userId);

            //then
            assertNotNull(tokens);
            assertEquals(accessToken, tokens.getAccessToken());
            assertEquals(refreshToken, tokens.getRefreshToken());
            assertEquals(accessTokenExpiredAt, tokens.getAccessToken().getExpiredAt());
            assertEquals(refreshTokenExpiredAt, tokens.getRefreshToken().getExpiredAt());
        }
    }

    @Nested
    class ReIssueAccessToken {

        @DisplayName("유효하거나_만료되지_않은_액세스_토큰일_경우_예외_반환")
        @Test
        void test1() {
            //given
            String invalidAccessToken = "invalid_access_token";
            String refreshToken = "refresh_token";

            when(jwtTokenParser.getRefreshTokenUserId(eq(invalidAccessToken), eq(refreshToken))).thenThrow(
                new BeApplicationException(ErrorCodes.TOKEN_NOT_EXPIRED_ACCESS_TOKEN, HttpStatus.BAD_REQUEST)
            );

            // when & then
            BeApplicationException exception = assertThrows(BeApplicationException.class, () ->
                sut.reIssueAccessToken(invalidAccessToken, refreshToken)
            );
            assertEquals(ErrorCodes.TOKEN_NOT_EXPIRED_ACCESS_TOKEN, exception.getErrorCodes());
        }

        @DisplayName("유효하지_않거나_만료된_리프레시_토큰일_경우_예외_반환")
        @Test
        void test2() {
            //given
            String accessToken = "expired_access_token";
            String invalidRefreshToken = "invalid_refresh_token";

            when(jwtTokenParser.getRefreshTokenUserId(eq(accessToken), eq(invalidRefreshToken))).thenThrow(
                new BeApplicationException(ErrorCodes.TOKEN_INVALID_REFRESH_TOKEN, HttpStatus.BAD_REQUEST));


            //when
            BeApplicationException exception = assertThrows(BeApplicationException.class, () ->
                sut.reIssueAccessToken(accessToken, invalidRefreshToken)
            );

            //then
            assertEquals(ErrorCodes.TOKEN_INVALID_REFRESH_TOKEN, exception.getErrorCodes());
        }

        @DisplayName("성공 - 리프레시_토큰을_통해_새로운_액세스_토큰_재발급")
        @Test
        void test1000() {
            //given
            String accessToken = "access_token";
            String refreshToken = "refresh_token";
            Long userId = 1L;
            Token reIssueAccessToken = new Token("new_access_token", LocalDateTime.now().plusHours(1));

            when(jwtTokenParser.getRefreshTokenUserId(eq(accessToken), eq(refreshToken))).thenReturn(userId);
            when(jwtTokenGenerator.issueAccessToken(userId)).thenReturn(reIssueAccessToken);

            //when
            Token result = sut.reIssueAccessToken(accessToken, refreshToken);

            //then
            assertNotNull(result);
            assertEquals(result.getToken(), reIssueAccessToken.getToken());
            assertEquals(result.getExpiredAt(), reIssueAccessToken.getExpiredAt());
        }
    }
}