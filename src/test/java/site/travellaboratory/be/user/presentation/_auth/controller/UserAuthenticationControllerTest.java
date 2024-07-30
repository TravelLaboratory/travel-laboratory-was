package site.travellaboratory.be.user.presentation._auth.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.common.presentation.config.JsonConfig;
import site.travellaboratory.be.common.presentation.resolver.AuthenticatedUserIdResolver;
import site.travellaboratory.be.test.assertion.Assertions;
import site.travellaboratory.be.user.application._auth.UserAuthenticationService;
import site.travellaboratory.be.user.application._auth.command.LoginCommand;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain._auth.Token;
import site.travellaboratory.be.user.domain._auth.Tokens;
import site.travellaboratory.be.user.domain._auth.request.LoginRequest;
import site.travellaboratory.be.user.domain.enums.UserStatus;
import site.travellaboratory.be.user.infrastructure.jwt.interceptor.AuthorizationInterceptor;

@Import({JsonConfig.class, AuthenticatedUserIdResolver.class})
@WebMvcTest(UserAuthenticationController.class)
class UserAuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserAuthenticationService userAuthenticationService;

    @MockBean
    private AuthorizationInterceptor authorizationInterceptor;



    @BeforeEach
    void setUp() throws Exception {
        // 인터셉터 모킹 설정 - 토큰 검증 및 userId 설정
        given(authorizationInterceptor.preHandle(any(HttpServletRequest.class),
            any(HttpServletResponse.class), any(Object.class)))
            .willAnswer(invocation -> {
                HttpServletRequest request = invocation.getArgument(0);
                String accessToken = request.getHeader("authorization-token");
                if (accessToken == null) {
                    throw new BeApplicationException(ErrorCodes.TOKEN_AUTHORIZATION_TOKEN_NOT_FOUND,
                        HttpStatus.BAD_REQUEST);
                }
                Long userId = 1L; // Mock user ID
                RequestAttributes requestAttributes = Objects.requireNonNull(
                    RequestContextHolder.getRequestAttributes());
                requestAttributes.setAttribute("userId", userId, RequestAttributes.SCOPE_REQUEST);
                return true;
            });
    }

    @Nested
    class Login {

        private User user;

        @BeforeEach
        void setUp() {
            this.user = User.builder()
                .id(1L)
                .username("test1234@email.com")
                .password("test1234!!")
                .nickname("userA")
                .profileImgUrl("https://profile_img.png")
                .status(UserStatus.ACTIVE)
                .isAgreement(true)
                .build();
        }

        @DisplayName("[실패] 유효하지 않는 Username인 경우 - 404 Not Found 반환")
        @Test
        void test1() throws Exception {
            //given
            String username = "invalid@email.com";
            String password = "password";
            LoginRequest invalidRequest = LoginRequest.builder()
                .username(username)
                .password(password)
                .build();

            given(userAuthenticationService.login(invalidRequest))
                .willThrow(new BeApplicationException(ErrorCodes.LOGIN_USERNAME_NOT_FOUND, HttpStatus.NOT_FOUND)
            );


            //when
            MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isNotFound())
                .andReturn();

            //then
            Assertions.assertMvcErrorEquals(result, ErrorCodes.LOGIN_USERNAME_NOT_FOUND);
            verify(userAuthenticationService).login(invalidRequest);
        }


        @DisplayName("[실패] 비밀번호가 일치하지 않음 - 401 Unauthorized 반환")
        @Test
        void test2() throws Exception {
            //given
            String username = user.getUsername();
            String invalidPassword = "wrong_password";
            LoginRequest invalidRequest = LoginRequest.builder()
                .username(username)
                .password(invalidPassword)
                .build();

            given(userAuthenticationService.login(invalidRequest))
                .willThrow(new BeApplicationException(ErrorCodes.AUTH_INVALID_PASSWORD, HttpStatus.UNAUTHORIZED)
                );

            //when
            MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isUnauthorized())
                .andReturn();

            //then
            Assertions.assertMvcErrorEquals(result, ErrorCodes.AUTH_INVALID_PASSWORD);
            verify(userAuthenticationService).login(invalidRequest);
        }

        @DisplayName("[성공] 로그인 - 200 OK 반환")
        @Test
        void loginSuccess() throws Exception {
            //given
            LocalDateTime currentTime = LocalDateTime.now();
            Tokens tokens = Tokens.builder()
                .accessToken(Token.builder()
                    .token("access-token")
                    .expiredAt(currentTime.plusDays(1))
                    .build())
                .refreshToken(Token.builder()
                    .token("refresh-token")
                    .expiredAt(currentTime.plusDays(2))
                    .build())
                .build();

            LoginCommand loginCommand = LoginCommand.from(user, tokens);

            LoginRequest request = LoginRequest.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();

            given(userAuthenticationService.login(any(LoginRequest.class))).willReturn(loginCommand);

            //when
            MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(
                    header().string("authorization-token", loginCommand.accessToken().getToken()))
                .andExpect(header().string("authorization-token-expired-at",
                    loginCommand.accessToken().getExpiredAt().toString()))
                .andExpect(header().string("refresh-token", loginCommand.refreshToken().getToken()))
                .andReturn();

            //then
            Assertions.assertMvcDataEquals(result, dataField -> {
                assertEquals(user.getId(), dataField.get("user_id").asLong());
                assertEquals(user.getNickname(), dataField.get("nickname").asText());
                assertEquals(user.getProfileImgUrl(), dataField.get("profile_img_url").asText());
            });
        }
    }

    @Nested
    class refreshAccessToken {
        @DisplayName("")
        @Test
        void test() {
            //given

            //when

            //then

        }

    }
}