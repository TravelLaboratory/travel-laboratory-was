package site.travellaboratory.be.user.application._auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.test.mock.user.FakeUserRepository;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain._auth.request.UserNicknameRequest;
import site.travellaboratory.be.user.domain._auth.request.UsernameRequest;
import site.travellaboratory.be.user.domain.enums.UserStatus;

class UserVerificationServiceTest {

    private UserVerificationService sut;
    private FakeUserRepository fakeUserRepository;

    @BeforeEach
    void setUp() {
        fakeUserRepository = new FakeUserRepository();
        this.sut = new UserVerificationService(fakeUserRepository);
    }

    @Nested
    class isNicknameAvailable {

        private final String existingNickname = "existing_nickname";

        @BeforeEach
        void setUp() {
            fakeUserRepository.save(User.builder()
                .nickname(existingNickname)
                .build()
            );
        }

        @DisplayName("이미_존재하는_nickname_인_경우")
        @Test
        void test1() {
            //given
            UserNicknameRequest invalidRequest = UserNicknameRequest.builder()
                .nickname(existingNickname).build();

            //when & then
            BeApplicationException exception = assertThrows(BeApplicationException.class, () ->
                sut.isNicknameAvailable(invalidRequest)
            );
            assertEquals(ErrorCodes.AUTH_DUPLICATED_NICK_NAME, exception.getErrorCodes());
        }

        @DisplayName("성공 - 존재하지_않는_nickname_인_경우")
        @Test
        void test1000() {
            //given
            String newNickname = "new_nickname";
            UserNicknameRequest validRequest = UserNicknameRequest.builder().nickname(newNickname)
                .build();

            //when & then
            Boolean result = sut.isNicknameAvailable(validRequest);
            assertTrue(result);
        }
    }

    @Nested
    class isUsernameAvailable {

        private final String existingUsername = "existing_username";

        @BeforeEach
        void setUp() {
            fakeUserRepository.save(User.builder()
                .username(existingUsername)
                .status(UserStatus.ACTIVE)
                .build()
            );
        }

        @DisplayName("이미_존재하는_username_인_경우")
        @Test
        void test1() {
            //given
            UsernameRequest invalidRequest = UsernameRequest.builder().username(existingUsername)
                .build();

            //when & then
            BeApplicationException exception = assertThrows(BeApplicationException.class, () ->
                sut.isUsernameAvailable(invalidRequest)
            );
            assertEquals(ErrorCodes.AUTH_DUPLICATED_USER_NAME, exception.getErrorCodes());
        }

        @DisplayName("성공 - 존재하지_않는_username_인_경우")
        @Test
        void test1000() {
            //given
            String newUserName = "new_username";
            UsernameRequest validRequest = UsernameRequest.builder().username(newUserName).build();

            //when & then
            Boolean result = sut.isUsernameAvailable(validRequest);
            assertTrue(result);
        }
    }
}
