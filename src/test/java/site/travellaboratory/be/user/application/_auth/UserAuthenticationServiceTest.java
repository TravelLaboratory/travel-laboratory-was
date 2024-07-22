package site.travellaboratory.be.user.application._auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import site.travellaboratory.be.common.exception.NotImplementedTestException;
import site.travellaboratory.be.mock.user.FakeUserRepository;
import site.travellaboratory.be.mock.user._auth.FakeTokenManager;

class UserAuthenticationServiceTest {

    private UserAuthenticationService sut;
    private BCryptPasswordEncoder encoder;
    private FakeTokenManager fakeTokenManager;
    private FakeUserRepository fakeUserRepository;

    @BeforeEach
    void setUp() {
        encoder = new BCryptPasswordEncoder();
        fakeTokenManager = new FakeTokenManager();
        fakeUserRepository = new FakeUserRepository();
        this.sut = new UserAuthenticationService(encoder, fakeTokenManager, fakeUserRepository);
    }

    @Nested
    class login {
        @DisplayName("존재하지_않거나_유효하지_않은_유저가_로그인할_경우")
        @Test
        void test1() {
            //given
            throw new  NotImplementedTestException();
            //when

            //then

        }

        @DisplayName("유저가_존재하지만_비밀번호가_틀린_경우")
        @Test
        void test2() {
            //given
            throw new  NotImplementedTestException();
            //when

            //then

        }

        @DisplayName("성공 - 유효한_이메일이고_비밀번호가_일치하는_경우_로그인_성공")
        @Test
        void test3() {
            //given
            throw new  NotImplementedTestException();
            //when

            //then

        }
    }

    @Nested
    class reIssueAccessToken {
        @DisplayName("성공 - 액세스_토큰_재발급")
        @Test
        void test1000() {
            //given
            throw new  NotImplementedTestException();
            //when

            //then

        }
    }
}