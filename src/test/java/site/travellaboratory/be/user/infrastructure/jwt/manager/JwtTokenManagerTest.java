//package site.travellaboratory.be.user.infrastructure.jwt.manager;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.when;
//
//import java.time.LocalDateTime;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import site.travellaboratory.be.common.exception.BeApplicationException;
//import site.travellaboratory.be.common.exception.ErrorCodes;
//import site.travellaboratory.be.user.domain._auth.Token;
//import site.travellaboratory.be.user.domain._auth.AuthTokens;
//import site.travellaboratory.be.user.infrastructure.jwt.manager.helper.JwtTokenGenerator;
//import site.travellaboratory.be.user.infrastructure.jwt.manager.helper.JwtTokenParser;
//import site.travellaboratory.be.user.infrastructure.jwt.manager.helper.JwtTokenValidator;
//
//@ExtendWith(MockitoExtension.class)
//class JwtTokenManagerTest {
//
//    @InjectMocks
//    private JwtTokenManager sut;
//
//    @Mock
//    private JwtTokenGenerator jwtTokenGenerator;
//
//    @Mock
//    private JwtTokenValidator jwtTokenValidator;
//
//    @Mock
//    private JwtTokenParser jwtTokenParser;
//
//    private final Long accessTokenPlusHour = 1L;
//    private final Long refreshTokenPlusHour = 2L;
//
//    @BeforeEach
//    void setUp() {
//        sut = new JwtTokenManager(jwtTokenGenerator, jwtTokenValidator, jwtTokenParser,
//            accessTokenPlusHour, refreshTokenPlusHour);
//    }
//
//    @Nested
//    class GenerateTokens {
//        @DisplayName("성공 - 토큰 생성")
//        @Test
//        void test1000() {
//            //given
//            Long userId = 1L;
//            String accessToken = "access_token";
//            String refreshToken = "refresh_token";
//
//            when(jwtTokenGenerator.issueToken(eq(userId), eq(accessTokenPlusHour))).thenReturn(accessToken);
//            when(jwtTokenGenerator.issueToken(eq(userId), eq(refreshTokenPlusHour))).thenReturn(refreshToken);
//
//            //when
//            AuthTokens authTokens = sut.generateTokens(userId);
//
//            //then
//            assertEquals(accessToken, authTokens.getAccessToken());
//            assertEquals(refreshToken, authTokens.getRefreshToken());
//            assertTrue(LocalDateTime.parse(authTokens.getExpiredAt()).isAfter(LocalDateTime.now()));
//        }
//    }
//
//    @Nested
//    class ReIssueToken {
//        @DisplayName("유효하거나_만료되지_않은_액세스_토큰일_경우_예외_반환")
//        @Test
//        void test1() {
//            //given
//            String accessToken = "access_token";
//            String refreshToken = "refresh_token";
//
//            when(jwtTokenValidator.isValidExpiredWithThrow(eq(accessToken))).thenReturn(false);
//
//            // when & then
//            BeApplicationException exception = assertThrows(BeApplicationException.class, () ->
//                sut.reIssueAccessToken(accessToken, refreshToken)
//            );
//            assertEquals(ErrorCodes.TOKEN_NOT_EXPIRED_ACCESS_TOKEN, exception.getErrorCodes());
//        }
//
//        @DisplayName("유효하지_않거나_만료된_리프레시_토큰일_경우_예외_반환")
//        @Test
//        void test2() {
//            //given
//            String accessToken = "access_token";
//            String refreshToken = "refresh_token";
//
//            when(jwtTokenValidator.isValidExpiredWithThrow(eq(accessToken))).thenReturn(true);
//            doThrow(new BeApplicationException(ErrorCodes.TOKEN_INVALID_REFRESH_TOKEN, HttpStatus.BAD_REQUEST))
//                .when(jwtTokenValidator).validRefreshTokenWithThrow(eq(refreshToken));
//
//            // when & then
//            BeApplicationException exception = assertThrows(BeApplicationException.class, () ->
//                sut.reIssueAccessToken(accessToken, refreshToken)
//            );
//            assertEquals(ErrorCodes.TOKEN_INVALID_REFRESH_TOKEN, exception.getErrorCodes());
//        }
//
//        @DisplayName("성공 - 리프레시_토큰을_통해_새로운_액세스_토큰_재발급")
//        @Test
//        void test1000() {
//            //given
//            String accessToken = "access_token";
//            String refreshToken = "refresh_token";
//            Long userId = 1L;
//            String newAccessToken = "new_access_token";
//
//            when(jwtTokenValidator.isValidExpiredWithThrow(eq(accessToken))).thenReturn(true);
//            doNothing().when(jwtTokenValidator).validRefreshTokenWithThrow(eq(refreshToken));
//            when(jwtTokenParser.getUserIdBy(eq(refreshToken))).thenReturn(userId);
//            when(jwtTokenGenerator.issueToken(eq(userId), eq(accessTokenPlusHour))).thenReturn(newAccessToken);
//
//            //when
//            Token reIssueToken = sut.reIssueAccessToken(accessToken, refreshToken);
//
//            //then
//            assertNotNull(reIssueToken);
//            assertEquals(newAccessToken, reIssueToken.getAccessToken());
//            assertTrue(LocalDateTime.parse(reIssueToken.getExpiredAt()).isAfter(LocalDateTime.now()));
//        }
//    }
//}