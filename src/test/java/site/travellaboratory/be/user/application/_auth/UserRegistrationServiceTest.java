package site.travellaboratory.be.user.application._auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import site.travellaboratory.be.mock.user.FakeUserRepository;
import site.travellaboratory.be.mock.user._auth.FakePwAnswerRepository;
import site.travellaboratory.be.mock.user._auth.FakePwQuestionRepository;

class UserRegistrationServiceTest {

    private UserRegistrationService sut;
    private BCryptPasswordEncoder encoder;
    @BeforeEach
    void setUp() {
        encoder = new BCryptPasswordEncoder();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        FakePwQuestionRepository fakePwQuestionRepository = new FakePwQuestionRepository();
        FakePwAnswerRepository fakePwAnswerRepository = new FakePwAnswerRepository();
        this.sut = new UserRegistrationService(
            encoder, fakeUserRepository, fakePwQuestionRepository, fakePwAnswerRepository);
    }

    @Nested
    class register {
        @DisplayName("이미_존재하는_nickname_인_경우")
        @Test
        void test1() {
            //given

            //when

            //then

        }

        @DisplayName("이미_존재하는_username_인_경우")
        @Test
        void test2() {
            //given

            //when

            //then

        }

        @DisplayName("유효하지_않은_비밀번호찾기용_질문을_선택한_경우")
        @Test
        void test3() {
            //given

            //when

            //then

        }

        @DisplayName("성공 - 회원가입")
        @Test
        void test1000() {
            //given

            //when

            //then

        }
    }
}