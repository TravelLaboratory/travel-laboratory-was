package site.travellaboratory.be.user.application._auth;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.mock.user.FakeUserRepository;
import site.travellaboratory.be.mock.user._auth.FakeTokenManager;
import site.travellaboratory.be.user.application._auth.command.LoginCommand;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain._auth.AccessToken;
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
        void setUP() {
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
            assertThrows(BeApplicationException.class, () ->
                sut.login(invalidRequest), ErrorCodes.AUTH_USER_NOT_FOUND.getMessage()
            );
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
            assertThrows(BeApplicationException.class, () ->
                sut.login(invalidRequest), ErrorCodes.AUTH_INVALID_PASSWORD.getMessage()
            );
        }

        @DisplayName("성공 - 유효한_이메일이고_비밀번호가_일치하는_경우_로그인_성공")
        @Test
        void test1000() {
            //given
            LoginRequest invalidRequest = LoginRequest.builder()
                .username(existingUsername)
                .password(correctPassword)
                .build();

            //when
            LoginCommand result = sut.login(invalidRequest);

            //then
            assertNotNull(result);
            assertNotNull(result.accessToken());
            assertNotNull(result.refreshToken());
            assertNotNull(result.expiredAt());
        }
    }

    @Nested
    class reIssueAccessToken {
        @DisplayName("성공 - 액세스_토큰_재발급")
        @Test
        void test1000() {
            // given
            String accessToken = "";
            String refreshToken = "";

            // when
            AccessToken result = sut.reIssueAccessToken(accessToken, refreshToken);

            // then
            assertNotNull(result);
            assertNotEquals(accessToken, result.getAccessToken());
            assertNotNull(result.getExpiredAt());
        }
    }
}