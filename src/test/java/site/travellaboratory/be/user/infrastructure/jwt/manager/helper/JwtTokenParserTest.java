package site.travellaboratory.be.user.infrastructure.jwt.manager.helper;

import static org.junit.jupiter.api.Assertions.*;

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
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;

class JwtTokenParserTest {

    private static final String SECRET_KEY = "random-test-secret-key-random-test-secret-key";
    private JwtTokenParser sut;
    private Key key;

    @BeforeEach
    void setUp() {
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        JwtTokenValidator jwtTokenValidator = new JwtTokenValidator(SECRET_KEY);
        this.sut = new JwtTokenParser(jwtTokenValidator);
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
    class getRefreshTokenUserId {

        private final Long userId = 1L;

        @DisplayName("만료되지_않은_액세스_토큰일_경우_예외_반환")
        @Test
        void test1() {
            //given
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime expiredAt = currentTime.plusHours(1);
            Date invalidExpiration = Date.from(expiredAt.atZone(ZoneId.systemDefault()).toInstant());
            String accessToken = createToken(userId, invalidExpiration);

            String refreshToken = "refresh_token";

            //when
            BeApplicationException exception = assertThrows(BeApplicationException.class, () ->
                sut.getRefreshTokenUserId(accessToken, refreshToken)
            );

            //then
            assertEquals(ErrorCodes.TOKEN_NOT_EXPIRED_ACCESS_TOKEN, exception.getErrorCodes());
        }
        @DisplayName("만료된_액세스_토큰이지만_유효하지_않은_리프레시_토큰일_경우_예외_반환")
        @Test
        void test2() {
            //given
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime invalidExpiredAt = currentTime.minusHours(1);
            Date invalidExpiration = Date.from(invalidExpiredAt.atZone(ZoneId.systemDefault()).toInstant());
            String invalidAccessToken = createToken(userId, invalidExpiration);

            String refreshToken = "refresh_token";

            //when
            BeApplicationException exception = assertThrows(BeApplicationException.class, () ->
                sut.getRefreshTokenUserId(invalidAccessToken, refreshToken));

            //then
            assertEquals(ErrorCodes.TOKEN_INVALID_REFRESH_TOKEN, exception.getErrorCodes());
        }

        @DisplayName("만료된_액세스_토큰이고_만료된_리프레시_토큰일_경우_예외_반환_(재로그인_해야_하는_경우)")
        @Test
        void test3() {
            //given
            LocalDateTime currentTime = LocalDateTime.now();

            LocalDateTime invalidAccessTokenExpiredAt = currentTime.minusHours(2);
            Date invalidAccessTokenExpiration = Date.from(invalidAccessTokenExpiredAt.atZone(ZoneId.systemDefault()).toInstant());
            String invalidAccessToken = createToken(userId, invalidAccessTokenExpiration);

            LocalDateTime invalidRefreshTokenExpiredAt = currentTime.minusHours(1);
            Date invalidRefreshTokenExpiration = Date.from(invalidRefreshTokenExpiredAt.atZone(ZoneId.systemDefault()).toInstant());
            String invalidRefreshToken = createToken(userId, invalidRefreshTokenExpiration);

            //when
            BeApplicationException exception = assertThrows(BeApplicationException.class, () ->
                sut.getRefreshTokenUserId(invalidAccessToken, invalidRefreshToken));

            //then
            assertEquals(ErrorCodes.REFRESH_TOKEN_EXPIRED_TOKEN, exception.getErrorCodes());
        }


        @DisplayName("성공 - 만료된_액세스_토큰이고_유효한_리프레시_토큰일_경우_userId_반환")
        @Test
        void test1000() {
            //given
            LocalDateTime currentTime = LocalDateTime.now();

            LocalDateTime accessTokenExpiredAt = currentTime.minusHours(1);
            Date expiredAccessTokenExpiration = Date.from(accessTokenExpiredAt.atZone(ZoneId.systemDefault()).toInstant());
            String expiredAccessToken = createToken(userId, expiredAccessTokenExpiration);

            LocalDateTime refreshTokenExpiredAt = currentTime.plusHours(2);
            Date refreshTokenExpiration = Date.from(refreshTokenExpiredAt.atZone(ZoneId.systemDefault()).toInstant());
            String refreshToken = createToken(userId, refreshTokenExpiration);

            //when
            Long refreshTokenUserId = sut.getRefreshTokenUserId(expiredAccessToken, refreshToken);

            //then
            assertEquals(userId, refreshTokenUserId);
        }
    }

    //    @Nested
//    class getUserIdBy {
//        private final Long userId = 1L;
//
//        @DisplayName("유효하지_않은_액세스_토큰일_경우_에러_반환")
//        @Test
//        void test1() {
//            //given
//            String invalidAccessToken = "invalid_access_token";
//
//            //when
//            BeApplicationException exception = assertThrows(BeApplicationException.class,
//                () -> sut.getUserIdBy(invalidAccessToken));
//
//            //then
//            assertEquals(ErrorCodes.TOKEN_INVALID_TOKEN, exception.getErrorCodes());
//        }
//
//        @DisplayName("만료된_액세스_토큰일_경우_에러_반환")
//        @Test
//        void test2() {
//            //given
//            LocalDateTime currentTime = LocalDateTime.now();
//            LocalDateTime invalidExpiredAt = currentTime.minusHours(1);
//            Date invalidExpiration = Date.from(invalidExpiredAt.atZone(ZoneId.systemDefault()).toInstant());
//            String invalidAccessToken = createToken(userId, invalidExpiration);
//
//            //when
//            BeApplicationException exception = assertThrows(BeApplicationException.class,
//                () -> sut.getUserIdBy(invalidAccessToken));
//
//            //then
//            assertEquals(ErrorCodes.TOKEN_EXPIRED_TOKEN, exception.getErrorCodes());
//        }
//
//        @DisplayName("성공 - 유효한_액세스_토큰으로_userId_반환")
//        @Test
//        void test1000() {
//            //given
//            LocalDateTime currentTime = LocalDateTime.now();
//            LocalDateTime expiredAt = currentTime.plusHours(1);
//            Date invalidExpiration = Date.from(expiredAt.atZone(ZoneId.systemDefault()).toInstant());
//            String accessToken = createToken(userId, invalidExpiration);
//
//            //when
//            Long tokenByUserId = sut.getUserIdBy(accessToken);
//
//            //then
//            assertEquals(userId, tokenByUserId);
//        }
//    }
}