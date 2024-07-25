package site.travellaboratory.be.user.application._auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.test.mock.user._auth.FakePwAnswerRepository;
import site.travellaboratory.be.test.mock.user.FakeUserRepository;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain._pw.PwAnswer;
import site.travellaboratory.be.user.domain._pw.enums.PwAnswerStatus;
import site.travellaboratory.be.user.domain.enums.UserStatus;
import site.travellaboratory.be.user.presentation._auth.request.PwInquiryEmailRequest;
import site.travellaboratory.be.user.presentation._auth.request.PwInquiryRenewalRequest;
import site.travellaboratory.be.user.presentation._auth.request.PwInquiryVerificationRequest;

class PwInquiryServiceTest {

    private PwInquiryService sut;
    private FakeUserRepository fakeUserRepository;
    private FakePwAnswerRepository fakePwAnswerRepository;
    private BCryptPasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        fakeUserRepository = new FakeUserRepository();
        fakePwAnswerRepository = new FakePwAnswerRepository();
        encoder = new BCryptPasswordEncoder();
        this.sut = new PwInquiryService(encoder, fakeUserRepository, fakePwAnswerRepository);
    }

    @Nested
    class PwInquiryEmail {

        private final String existingUsername = "existing_username";
        private final Long pwQuestionId = 1L;

        @BeforeEach
        void setUp() {
            // user 저장
            User savedUser = fakeUserRepository.save(User.builder()
                .username(existingUsername)
                .status(UserStatus.ACTIVE)
                .build());

            fakePwAnswerRepository.save(PwAnswer.builder()
                .userId(savedUser.getId()) // 저장한 user id 사용
                .pwQuestionId(pwQuestionId)
                .answer("pw_answer")
                .status(PwAnswerStatus.ACTIVE)
                .build());
        }

        @DisplayName("비밀번호_찾기_시에_유효하지_않는_이메일인_경우")
        @Test
        void test1() {
            //given
            PwInquiryEmailRequest invalidRequest = PwInquiryEmailRequest.builder()
                .username("non_existing_username")
                .build();

            //when & then
            BeApplicationException exception = assertThrows(BeApplicationException.class, () ->
                sut.pwInquiryEmail(invalidRequest)
            );
            assertEquals(ErrorCodes.PASSWORD_INVALID_EMAIL, exception.getErrorCodes());
        }

        @DisplayName("성공 - 비밀번호_찾기_시에_유효한_이메일에_대해_비밀번호찾기용_질문_반환")
        @Test
        void test1000() {
            //given
            PwInquiryEmailRequest request = PwInquiryEmailRequest.builder()
                .username(existingUsername)
                .build();

            //when
            PwAnswer result = sut.pwInquiryEmail(request);

            //then
            assertEquals(existingUsername, request.username());
            assertEquals(pwQuestionId, result.getPwQuestionId());
        }
    }

    @Nested
    class PwInquiryVerification {

        private final String existingUsername = "existing_username";
        private final Long pwQuestionId = 1L;
        private final String correctAnswer = "correct_answer";

        @BeforeEach
        void setUp() {
            // user 저장
            User savedUser = fakeUserRepository.save(User.builder()
                .username(existingUsername)
                .status(UserStatus.ACTIVE)
                .build());

            PwAnswer pwAnswer = PwAnswer.builder()
                .userId(savedUser.getId()) // 저장한 user id 사용
                .pwQuestionId(pwQuestionId)
                .answer(correctAnswer)
                .status(PwAnswerStatus.ACTIVE)
                .build();
            fakePwAnswerRepository.save(pwAnswer);
        }

        @DisplayName("비밀번호_검증_시에_유효하지_않는_이메일인_경우")
        @Test
        void test1() {
            //given
            PwInquiryVerificationRequest invalidRequest = PwInquiryVerificationRequest.builder()
                .username("non_existing_username")
                .build();

            //when & then
            BeApplicationException exception = assertThrows(BeApplicationException.class, () ->
                sut.pwInquiryVerification(invalidRequest)
            );
            assertEquals(ErrorCodes.PASSWORD_INVALID_EMAIL, exception.getErrorCodes());
        }

        @DisplayName("비밀번호_검증_시에_유효하지_않는_답변인_경우")
        @Test
        void test2() {
            //given
            PwInquiryVerificationRequest invalidRequest = PwInquiryVerificationRequest.builder()
                .username(existingUsername)
                .pwQuestionId(pwQuestionId)
                .answer("wrong_answer")
                .build();

            //when & then
            BeApplicationException exception = assertThrows(BeApplicationException.class, () ->
                sut.pwInquiryVerification(invalidRequest)
            );
            assertEquals(ErrorCodes.PASSWORD_INQUIRY_INVALID_ANSWER, exception.getErrorCodes());
        }

        @DisplayName("성공 - 비밀번호_검증_시에_유효한_이메일과_답변이_일치하는_경우")
        @Test
        void test1000() {
            //given
            PwInquiryVerificationRequest request = PwInquiryVerificationRequest.builder()
                .username(existingUsername)
                .pwQuestionId(pwQuestionId)
                .answer(correctAnswer)
                .build();

            //when
            PwAnswer result = sut.pwInquiryVerification(request);

            //then
            assertEquals(existingUsername, request.username());
            assertEquals(pwQuestionId, result.getPwQuestionId());
            assertEquals(correctAnswer, result.getAnswer());
        }
    }

    @Nested
    class PwInquiryRenewal {

        private final String existingUsername = "existing_username";
        private final Long pwQuestionId = 1L;
        private final String correctAnswer = "correct_answer";
        private final String newPassword = "new_password";

        @BeforeEach
        void setUp() {
            User user = User.builder()
                .username(existingUsername)
                .status(UserStatus.ACTIVE)
                .build();
            User savedUser = fakeUserRepository.save(user); // 저장된 객체를 반환받아 id를 설정

            PwAnswer pwAnswer = PwAnswer.builder()
                .userId(savedUser.getId()) // 저장된 객체의 id를 사용
                .pwQuestionId(pwQuestionId)
                .answer(correctAnswer)
                .status(PwAnswerStatus.ACTIVE)
                .build();
            fakePwAnswerRepository.save(pwAnswer);
        }

        @DisplayName("비밀번호_재설정_시에_유효하지_않는_이메일인_경우")
        @Test
        void test1() {
            //given
            PwInquiryRenewalRequest invalidRequest = PwInquiryRenewalRequest.builder()
                .username("non_existing_username")
                .pwQuestionId(pwQuestionId)
                .answer(correctAnswer)
                .password(newPassword)
                .build();

            //when & then
            BeApplicationException exception = assertThrows(BeApplicationException.class, () ->
                sut.pwInquiryRenewal(invalidRequest)
            );
            assertEquals(ErrorCodes.PASSWORD_INVALID_EMAIL, exception.getErrorCodes());
        }

        @DisplayName("비밀번호_재설정_시에_유효하지_않는_답변인_경우")
        @Test
        void test2() {
            //given
            PwInquiryRenewalRequest invalidRequest = PwInquiryRenewalRequest.builder()
                .username(existingUsername)
                .pwQuestionId(pwQuestionId)
                .answer("wrong_answer")
                .password(newPassword)
                .build();

            //when & then
            BeApplicationException exception = assertThrows(BeApplicationException.class, () ->
                sut.pwInquiryRenewal(invalidRequest)
            );
            assertEquals(ErrorCodes.PASSWORD_INQUIRY_INVALID_ANSWER, exception.getErrorCodes());
        }

        @DisplayName("성공 - 비밀번호_재설정_시에_유효한_이메일과_답변이_일치하는_경우")
        @Test
        void test1000() {
            //given
            PwInquiryRenewalRequest request = PwInquiryRenewalRequest.builder()
                .username(existingUsername)
                .pwQuestionId(pwQuestionId)
                .answer(correctAnswer)
                .password(newPassword)
                .build();

            //when
            User updatedUser = sut.pwInquiryRenewal(request);

            //then
            assertEquals(existingUsername, updatedUser.getUsername());
            assertTrue(encoder.matches(newPassword, updatedUser.getPassword()));
        }
    }
}