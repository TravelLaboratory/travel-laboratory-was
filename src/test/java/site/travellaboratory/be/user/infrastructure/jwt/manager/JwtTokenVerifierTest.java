//package site.travellaboratory.be.user.infrastructure.jwt.manager;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.when;
//
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
//import site.travellaboratory.be.user.infrastructure.jwt.manager.helper.JwtTokenParser;
//import site.travellaboratory.be.user.infrastructure.jwt.manager.helper.JwtTokenValidator;
//
//@ExtendWith(MockitoExtension.class)
//class JwtTokenVerifierTest {
//    @InjectMocks
//    private JwtTokenVerifier sut;
//
//    @Mock
//    private JwtTokenValidator jwtTokenValidator;
//
//    @Mock
//    private JwtTokenParser jwtTokenParser;
//
//    @Nested
//    class GetTokenUserId {
//
//        @DisplayName("유효하지_않은_액세스_토큰일_경우_예외_반환")
//        @Test
//        void test1() {
//            //given
//            String invalidAccessToken = "invalid_access_token";
//
//            doThrow(
//                new BeApplicationException(ErrorCodes.TOKEN_INVALID_TOKEN, HttpStatus.BAD_REQUEST))
//                .when(jwtTokenValidator).validAccessTokenWithThrow(eq(invalidAccessToken));
//
//            //when
//            BeApplicationException exception = assertThrows(BeApplicationException.class,
//                () -> sut.getAccessTokenUserId(invalidAccessToken)
//            );
//
//            //then
//            assertEquals(ErrorCodes.TOKEN_INVALID_TOKEN, exception.getErrorCodes());
//        }
//
//        @DisplayName("만료된_액세스_토큰일_경우_예외_반환")
//        @Test
//        void test2() {
//            //given
//            String expiredAccessToken = "expired_access_token";
//
//            doThrow(
//                new BeApplicationException(ErrorCodes.TOKEN_EXPIRED_TOKEN, HttpStatus.UNAUTHORIZED))
//                .when(jwtTokenValidator).validAccessTokenWithThrow(eq(expiredAccessToken));
//
//            //when
//            BeApplicationException exception = assertThrows(BeApplicationException.class,
//                () -> sut.getAccessTokenUserId(expiredAccessToken)
//            );
//
//            //then
//            assertEquals(ErrorCodes.TOKEN_EXPIRED_TOKEN, exception.getErrorCodes());
//        }
//
//        @DisplayName("성공 - 유효한_액세스_토큰을_사용해_userId_반환")
//        @Test
//        void testValidAccessToken() {
//            //given
//            String accessToken = "valid_access_token";
//            Long expectedUserId = 1L;
//
//            when(jwtTokenParser.getUserIdBy(eq(accessToken))).thenReturn(expectedUserId);
//
//            //when
//            Long userId = sut.getAccessTokenUserId(accessToken);
//
//            //then
//            assertEquals(expectedUserId, userId);
//        }
//    }
//}