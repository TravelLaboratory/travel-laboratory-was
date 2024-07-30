package site.travellaboratory.be.user.application._auth;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.test.mock.user.FakeUserRepository;
import site.travellaboratory.be.test.mock.user._auth.FakeTokenManager;
import site.travellaboratory.be.user.application._auth.command.LoginCommand;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain._auth.Token;
import site.travellaboratory.be.user.domain._auth.request.LoginRequest;
import site.travellaboratory.be.user.domain.enums.UserStatus;

class UserAuthenticationServiceTest {

    private UserAuthenticationService sut;
    private BCryptPasswordEncoder encoder;
    private FakeUserRepository fakeUserRepository;

    @BeforeEach
    void setUp() {
        encoder = new BCryptPasswordEncoder();
        FakeTokenManager fakeTokenManager = new FakeTokenManager();
        fakeUserRepository = new FakeUserRepository();
        this.sut = new UserAuthenticationService(encoder, fakeTokenManager, fakeUserRepository);
    }

    @Nested
    class login {

        private final String existingUsername = "existing_username";
        private final String correctPassword = "correct_password";

        @BeforeEach
        void setUp() {
            User user = User.builder()
                .username(existingUsername)
                .password(encoder.encode(correctPassword))
                .status(UserStatus.ACTIVE)
                .build();

            fakeUserRepository.save(user);
        }

        @DisplayName("존재하지_않거나_유효하지_않은_유저가_로그인할_경우")
        @Test
        void test1() {
            //given
            LoginRequest invalidRequest = LoginRequest.builder()
                .username("non_existing_username")
                .password("password")
                .build();

            //when & then
            BeApplicationException exception = assertThrows(BeApplicationException.class, () ->
                sut.login(invalidRequest)
            );
            assertEquals(ErrorCodes.LOGIN_USERNAME_NOT_FOUND, exception.getErrorCodes());
        }

        @DisplayName("유저가_존재하지만_비밀번호가_틀린_경우")
        @Test
        void test2() {
            //given
            LoginRequest invalidRequest = LoginRequest.builder()
                .username(existingUsername)
                .password("password")
                .build();

            //when & then
            BeApplicationException exception = assertThrows(BeApplicationException.class, () ->
                sut.login(invalidRequest)
            );
            assertEquals(ErrorCodes.AUTH_INVALID_PASSWORD, exception.getErrorCodes());
        }

        @DisplayName("성공 - 유효한_이메일이고_비밀번호가_일치하는_경우_로그인_성공")
        @Test
        void test1000() {
            //given
            LoginRequest validRequest = LoginRequest.builder()
                .username(existingUsername)
                .password(correctPassword)
                .build();

            //when
            LoginCommand result = sut.login(validRequest);

            //then
            assertNotNull(result);
            assertNotNull(result.accessToken().getToken());
            assertNotNull(result.refreshToken().getToken());
            assertNotNull(result.accessToken().getExpiredAt());
        }
    }

    @Nested
    class reIssueToken {
        @DisplayName("성공 - 액세스_토큰_재발급")
        @Test
        void test1000() {
            // given
            String accessToken = "";
            String refreshToken = "";

            // when
            Token result = sut.reIssueAccessToken(accessToken, refreshToken);

            // then
            assertNotNull(result);
            assertNotEquals(accessToken, result.getToken());
            assertNotNull(result.getExpiredAt());
        }
    }
}