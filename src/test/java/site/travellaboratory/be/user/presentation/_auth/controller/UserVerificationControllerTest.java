package site.travellaboratory.be.user.presentation._auth.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static site.travellaboratory.be.test.assertion.Assertions.assertMvcDataEquals;
import static site.travellaboratory.be.test.assertion.Assertions.assertMvcErrorEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import site.travellaboratory.be.user.application._auth.UserVerificationService;
import site.travellaboratory.be.user.domain._auth.request.UserNicknameRequest;
import site.travellaboratory.be.user.domain._auth.request.UsernameRequest;
import site.travellaboratory.be.user.infrastructure.jwt.interceptor.AuthorizationInterceptor;

@Import({JsonConfig.class, AuthenticatedUserIdResolver.class})
@WebMvcTest(UserVerificationController.class)
class UserVerificationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private UserVerificationService userVerificationService;

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
    @DisplayName("[POST] 닉네임 중복 확인 /api/v1/auth/nickname")
    class checkNicknameDuplicate {

        @DisplayName("[실패] 닉네임 사용 불가능 - 409 Conflict 반환")
        @Test
        void test1() throws Exception {
            // given
            String nickname = "existingNickname";
            UserNicknameRequest invalidRequest = UserNicknameRequest
                .builder()
                .nickname(nickname)
                .build();

            given(userVerificationService.isNicknameAvailable(invalidRequest))
                .willThrow(new BeApplicationException(ErrorCodes.AUTH_DUPLICATED_NICK_NAME,
                    HttpStatus.CONFLICT));

            // when
            MvcResult result = mockMvc.perform(post("/api/v1/auth/nickname")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isConflict())
                .andReturn();

            // then
            assertMvcErrorEquals(result, ErrorCodes.AUTH_DUPLICATED_NICK_NAME);
            verify(userVerificationService).isNicknameAvailable(any(UserNicknameRequest.class));
        }


        @DisplayName("[성공] 닉네임 사용 가능 - 200 OK 반환")
        @Test
        void test1000() throws Exception {
            // given
            String nickname = "uniqueNickname";
            UserNicknameRequest request = UserNicknameRequest
                .builder()
                .nickname(nickname)
                .build();
            given(userVerificationService.isNicknameAvailable(request))
                .willReturn(true);

            // when
            MvcResult result = mockMvc.perform(post("/api/v1/auth/nickname")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

            // then
            assertMvcDataEquals(result, dataField -> {
                assertTrue(dataField.get("is_available").asBoolean());
            });
            verify(userVerificationService).isNicknameAvailable(any(UserNicknameRequest.class));
        }
    }

    @Nested
    @DisplayName("[POST] 이메일(회원 아이디) 중복 확인 /api/v1/auth/username")
    class checkUsernameDuplicate {

        @DisplayName("[실패] 이메일(회원 아이디) 사용 불가능 - 409 Conflict 반환")
        @Test
        void test1() throws Exception {
            // given
            String existingUsername = "existingUsername";
            UsernameRequest request = UsernameRequest.builder()
                .username(existingUsername)
                .build();

            given(userVerificationService.isUsernameAvailable(any(UsernameRequest.class)))
                .willThrow(new BeApplicationException(ErrorCodes.AUTH_DUPLICATED_USER_NAME,
                    HttpStatus.CONFLICT));

            // when
            MvcResult result = mockMvc.perform(post("/api/v1/auth/username")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andReturn();

            // then
            assertMvcErrorEquals(result, ErrorCodes.AUTH_DUPLICATED_USER_NAME);
            verify(userVerificationService).isUsernameAvailable(any(UsernameRequest.class));
        }


        @DisplayName("[성공] 이메일(회원 아이디) 사용 가능 - 200 OK 반환")
        @Test
        void test1000() throws Exception {
            // given
            String username = "uniqueUsername";
            UsernameRequest request = UsernameRequest.builder()
                .username(username)
                .build();

            given(userVerificationService.isUsernameAvailable(any(UsernameRequest.class)))
                .willReturn(true);

            // when
            MvcResult result = mockMvc.perform(post("/api/v1/auth/username")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

            // then
            assertMvcDataEquals(result, dataField -> {
                assertTrue(dataField.get("is_available").asBoolean());
            });
            verify(userVerificationService).isUsernameAvailable(any(UsernameRequest.class));
        }
    }
}