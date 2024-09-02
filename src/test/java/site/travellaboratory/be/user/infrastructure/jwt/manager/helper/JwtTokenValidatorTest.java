package site.travellaboratory.be.user.infrastructure.jwt.manager.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import site.travellaboratory.be.common.presentation.exception.BeApplicationException;
import site.travellaboratory.be.common.presentation.error.ErrorCodes;

class JwtTokenValidatorTest {

    private static final String SECRET_KEY = "random-test-secret-key-random-test-secret-key";
    private JwtTokenValidator sut;
    private Key key;

    @BeforeEach
    void setUp() {
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.sut = new JwtTokenValidator(SECRET_KEY);
    }

    private String createToken(Long userId, Date expiration) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        return Jwts.builder()
            .setClaims(claims)
            .setExpiration(expiration)
            .signWith(key)
            .compact();
    }

    @Nested
    class ParseAccessTokenClaims {

        private final Long userId = 1L;

        @DisplayName("만료된_액세스_토큰일_경우_TOKEN_EXPIRED_TOKEN_에러_반환")
        @Test
        void test1() {
            //given
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime invalidExpiredAt = currentTime.minusHours(1);
            Date invalidExpiration = Date.from(invalidExpiredAt.atZone(ZoneId.systemDefault()).toInstant());
            String invalidAccessToken = createToken(userId, invalidExpiration);

            //when
            BeApplicationException exception = assertThrows(BeApplicationException.class, () ->
                sut.parseAccessTokenClaims(invalidAccessToken)
            );

            //then
            assertEquals(ErrorCodes.TOKEN_EXPIRED_TOKEN, exception.getErrorCodes());
        }

        @DisplayName("유효하지_않은_액세스_토큰_TOKEN_INVALID_TOKEN_에러_반환")
        @Test
        void test2() {
            //given
            String invalidAccessToken = "invalid_access_token";

            //when & then
            BeApplicationException exception = assertThrows(BeApplicationException.class, () ->
                sut.parseAccessTokenClaims(invalidAccessToken)
            );
            assertEquals(ErrorCodes.TOKEN_INVALID_TOKEN, exception.getErrorCodes());
        }

        @DisplayName("성공 - 유효한_액세스_토큰_파싱")
        @Test
        void test1000() {
            //given
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime expiredAt = currentTime.plusHours(1);
            Date expiration = Date.from(expiredAt.atZone(ZoneId.systemDefault()).toInstant());

            String validAccessToken = createToken( userId, expiration);

            //when
            Claims claims = sut.parseAccessTokenClaims(validAccessToken);

            //then
            assertEquals(userId, claims.get("userId", Long.class));
        }
    }

    @Nested
    class ParseRefreshTokenClaims {

        private final Long userId = 1L;

        @DisplayName("만료된_리프레시_토큰일_경우_REFRESH_TOKEN_EXPIRED_TOKEN_에러_반환")
        @Test
        void test1() {
            //given
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime invalidExpiredAt = currentTime.minusHours(1); // 만료 시간이 지남
            Date invalidExpiration = Date.from(invalidExpiredAt.atZone(ZoneId.systemDefault()).toInstant());

            String invalidRefreshToken = createToken(userId, invalidExpiration);

            //when
            BeApplicationException exception = assertThrows(BeApplicationException.class, () ->
                sut.parseRefreshTokenClaims(invalidRefreshToken)
            );

            //then
            assertEquals(ErrorCodes.REFRESH_TOKEN_EXPIRED_TOKEN, exception.getErrorCodes());
        }

        @DisplayName("유효하지_않은_리프레시_토큰일_경우_REFRESH_TOKEN_INVALID_REFRESH_TOKEN 에러 반환")
        @Test
        void test2() {
            //given
            String invalidRefreshToken = "invalid_refresh_token";

            //when
            BeApplicationException exception = assertThrows(BeApplicationException.class, () ->
                sut.parseRefreshTokenClaims(invalidRefreshToken)
            );

            //then
            assertEquals(ErrorCodes.REFRESH_TOKEN_INVALID_REFRESH_TOKEN, exception.getErrorCodes());
        }

        @DisplayName("성공 - 유효한_리프레시_토큰_파싱")
        @Test
        void test1000() {
            //given
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime expiredAt = currentTime.plusHours(2); // 만료 시간을 2시간 뒤로
            Date expiration = Date.from(expiredAt.atZone(ZoneId.systemDefault()).toInstant());

            String refreshToken = createToken(userId, expiration);

            //when
            Claims claims = sut.parseRefreshTokenClaims(refreshToken);

            //then
            assertEquals(userId, claims.get("userId", Long.class));
            LocalDateTime expiredLocalDateTime = claims.getExpiration().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            assertTrue(expiredLocalDateTime.isAfter(currentTime.plusHours(1)));
        }
    }

    @Nested
    class IsTokenExpired {

        private final Long userId = 1L;

        @DisplayName("만료된_액세스_토큰_true_반환")
        @Test
        void test1() {
            //given
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime invalidExpiredAt = currentTime.minusHours(1);
            Date invalidExpiration = Date.from(invalidExpiredAt.atZone(ZoneId.systemDefault()).toInstant());

            String invalidAccessToken = createToken(userId, invalidExpiration);

            //when
            boolean result = sut.isTokenExpired(invalidAccessToken);

            //then
            assertTrue(result);
        }

        @DisplayName("유효한_액세스_토큰_false_반환")
        @Test
        void test1000() {
            //given
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime expiredAt = currentTime.plusHours(1);
            Date expiration = Date.from(expiredAt.atZone(ZoneId.systemDefault()).toInstant());

            String validAccessToken = createToken(userId, expiration);

            //when
            boolean result = sut.isTokenExpired(validAccessToken);

            //then
            assertFalse(result);
        }
    }
}
