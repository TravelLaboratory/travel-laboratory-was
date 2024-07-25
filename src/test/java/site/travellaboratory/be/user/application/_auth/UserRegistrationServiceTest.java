package site.travellaboratory.be.user.application._auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.test.mock.user.FakeUserRepository;
import site.travellaboratory.be.test.mock.user._auth.FakePwAnswerRepository;
import site.travellaboratory.be.test.mock.user._auth.FakePwQuestionRepository;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain._auth.request.UserJoinRequest;
import site.travellaboratory.be.user.domain._pw.PwQuestion;
import site.travellaboratory.be.user.domain._pw.enums.PwQuestionStatus;
import site.travellaboratory.be.user.domain.enums.UserStatus;

class UserRegistrationServiceTest {

    private UserRegistrationService sut;
    private BCryptPasswordEncoder encoder;
    private FakeUserRepository fakeUserRepository;
    private FakePwQuestionRepository fakePwQuestionRepository;

    @BeforeEach
    void setUp() {
        encoder = new BCryptPasswordEncoder();
        fakeUserRepository = new FakeUserRepository();
        fakePwQuestionRepository = new FakePwQuestionRepository();
        FakePwAnswerRepository fakePwAnswerRepository = new FakePwAnswerRepository();
        this.sut = new UserRegistrationService(
            encoder, fakeUserRepository, fakePwQuestionRepository, fakePwAnswerRepository);
    }

    @Nested
    class register {

        private final String existingUsername = "existing_username";
        private final String existingNickname = "existing_nickname";
        private final String password = "password";
        private final Long existingPwQuestionId = 1L;

        @BeforeEach
        void setUp() {
            fakeUserRepository.save(User.builder()
                .username(existingUsername)
                .password(password)
                .nickname(existingNickname)
                .status(UserStatus.ACTIVE)
                .build());

            fakePwQuestionRepository.save(PwQuestion.builder()
                .id(existingPwQuestionId)
                .question("question")
                .status(PwQuestionStatus.ACTIVE)
                .build()
            );
        }

        @DisplayName("이미_존재하는_nickname_인_경우")
        @Test
        void test1() {
            //given
            UserJoinRequest invalidRequest = UserJoinRequest.builder()
                .password(password)
                .nickname(existingNickname)
                .isAgreement(true)
                .build();

            //when & then
            BeApplicationException exception = assertThrows(BeApplicationException.class, () ->
                sut.register(invalidRequest)
            );
            assertEquals(ErrorCodes.AUTH_DUPLICATED_NICK_NAME, exception.getErrorCodes());
        }

        @DisplayName("이미_존재하는_username_인_경우")
        @Test
        void test2() {
            //given
            UserJoinRequest invalidRequest = UserJoinRequest.builder()
                .username(existingUsername)
                .password(password)
                .nickname("non_existing_nickname")
                .isAgreement(true)
                .build();

            //when & then
            BeApplicationException exception = assertThrows(BeApplicationException.class, () ->
                sut.register(invalidRequest)
            );
            assertEquals(ErrorCodes.AUTH_DUPLICATED_USER_NAME, exception.getErrorCodes());
        }

        @DisplayName("유효하지_않은_비밀번호찾기용_질문을_선택한_경우")
        @Test
        void test3() {
            //given
            UserJoinRequest invalidRequest = UserJoinRequest.builder()
                .username("non_existing_nickname")
                .password(password)
                .nickname("non_existing_nickname")
                .pwQuestionId(99999L)
                .isAgreement(true)
                .build();

            //when & then
            BeApplicationException exception = assertThrows(BeApplicationException.class, () ->
                sut.register(invalidRequest)
            );
            assertEquals(ErrorCodes.PASSWORD_INVALID_QUESTION, exception.getErrorCodes());
        }

        @DisplayName("성공 - 회원가입")
        @Test
        void test1000() {
            //given
            UserJoinRequest validRequest = UserJoinRequest.builder()
                .username("new_username")
                .nickname("new_nickname")
                .password(password)
                .pwQuestionId(existingPwQuestionId)
                .pwAnswer("answer")
                .isAgreement(true)
                .build();

            //when
            User savedUser = sut.register(validRequest);

            //then
            assertNotNull(savedUser);
            assertEquals("new_username", savedUser.getUsername());
            assertEquals("new_nickname", savedUser.getNickname());
            assertTrue(encoder.matches(password, savedUser.getPassword()));
        }
    }
}
