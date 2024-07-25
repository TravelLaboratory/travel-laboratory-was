package site.travellaboratory.be.user.infrastructure.jwt.manager.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import site.travellaboratory.be.user.domain._auth.Token;

class JwtTokenGeneratorTest {

    private static final String SECRET_KEY = "random-test-secret-key-random-test-secret-key";
    private JwtTokenGenerator sut;
    private Key key;
    private final Long accessTokenPlusHour = 1L;
    private final Long refreshTokenPlusHour = 2L;
    @BeforeEach
    void setUp() {
        this.sut = new JwtTokenGenerator(SECRET_KEY, accessTokenPlusHour, refreshTokenPlusHour);

        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    @Nested
    class IssueAccessToken {

        @DisplayName("성공 - 유효한_userId로_액세스_토큰_생성")
        @Test
        void test1000() {
            //given
            Long userId = 1L;

            //when
            Token accessToken = sut.issueAccessToken(userId);

            //then
            assertNotNull(accessToken);

            // 검증을 위해 토큰 파싱
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken.getToken())
                .getBody();

            assertEquals(userId, claims.get("userId", Long.class));

            Date expiration = claims.getExpiration();
            LocalDateTime expiredLocalDateTime = expiration.toInstant().atZone(ZoneId.systemDefault())
                .toLocalDateTime();

            // todo: 임시로 10분 빼고 비교하는걸로
            // 만료 시간 검증
            assertTrue(expiredLocalDateTime.isAfter(LocalDateTime.now().plusHours(accessTokenPlusHour).minusMinutes(10)));
        }
    }

    @Nested
    class IssueRefreshToken {

        @DisplayName("성공 - 유효한_userId로_액세스_토큰_생성")
        @Test
        void test1000() {
            //given
            Long userId = 1L;

            //when
            Token refreshToken = sut.issueRefreshToken(userId);

            //then
            assertNotNull(refreshToken);

            // 검증을 위해 토큰 파싱
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(refreshToken.getToken())
                .getBody();

            assertEquals(userId, claims.get("userId", Long.class));

            Date expiration = claims.getExpiration();
            LocalDateTime expiredLocalDateTime = expiration.toInstant().atZone(ZoneId.systemDefault())
                .toLocalDateTime();

            // todo: 임시로 10분 빼고 비교하는걸로
            // 만료 시간 검증
            assertTrue(expiredLocalDateTime.isAfter(LocalDateTime.now().plusHours(refreshTokenPlusHour).minusMinutes(10)));
            assertTrue(expiredLocalDateTime.isAfter(LocalDateTime.now().plusHours(accessTokenPlusHour)));
        }
    }
}
