//package site.travellaboratory.be.user.infrastructure.jwt.manager.helper;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import java.nio.charset.StandardCharsets;
//import java.security.Key;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.Date;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//
//class JwtTokenGeneratorTest {
//
//    private static final String SECRET_KEY = "random-test-secret-key-random-test-secret-key";
//    private JwtTokenGenerator sut;
//    private Key key;
//
//    @BeforeEach
//    void setUp() {
//        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
//        this.key = Keys.hmacShaKeyFor(keyBytes);
//        this.sut = new JwtTokenGenerator(SECRET_KEY);
//    }
//
//    @Nested
//    class IssueToken {
//
//        @DisplayName("성공 - 유효한_userId로_JWT_토큰_생성")
//        @Test
//        void test1000() {
//            //given
//            Long userId = 1L;
//            Long tokenPlusHour = 1L;
//
//            //when
//            String token = sut.issueToken(userId, tokenPlusHour);
//
//            //then
//            assertNotNull(token);
//
//            // 검증을 위해 토큰 파싱
//            Claims claims = Jwts.parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//
//            assertEquals(userId, claims.get("userId", Long.class));
//
//            Date expiration = claims.getExpiration();
//            LocalDateTime expiredLocalDateTime = expiration.toInstant().atZone(ZoneId.systemDefault())
//                .toLocalDateTime();
//
//            // todo: 임시로 10분 빼고 비교하는걸로
//            // 만료 시간 검증
//            assertTrue(expiredLocalDateTime.isAfter(LocalDateTime.now().plusHours(tokenPlusHour).minusMinutes(10)));
//        }
//    }
//}
