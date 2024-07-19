package site.travellaboratory.be.user.domain._auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.user.domain._auth.enums.UserRole;
import site.travellaboratory.be.user.domain._auth.request.UserJoinRequest;

public class UserAuthTest {

    private final String encodedPassword = "encodedPassword";

    @Nested
    class register {

        @DisplayName("회원가입시_개인정보_미동의할_경우_예외_반환")
        @Test
        void test1() {
            //given
            UserJoinRequest joinRequest = UserJoinRequest.builder()
                .username("userA@email.com")
                .password("password")
                .nickname("userA")
                .pwQuestionId(1L)
                .pwAnswer("1번답")
                .isAgreement(false)
                .build();

            //when & then
            assertThrows(BeApplicationException.class, () -> {
                UserAuth.register(encodedPassword, joinRequest);
            });
        }

        @DisplayName("성공 - UserJoinRequest_으로_UserAuth_객체_생성")
        @Test
        void test1000() {
            //given
            UserJoinRequest joinRequest = UserJoinRequest.builder()
                .username("userA@email.com")
                .password("password")
                .nickname("userA")
                .pwQuestionId(1L)
                .pwAnswer("1번답")
                .isAgreement(true)
                .build();

            //when
            UserAuth userAuth = UserAuth.register(encodedPassword, joinRequest);

            //then
            assertEquals(joinRequest.username(), userAuth.getUsername());
            assertEquals(encodedPassword, userAuth.getPassword());
            assertEquals(UserRole.USER, userAuth.getRole());
            assertEquals(joinRequest.isAgreement(), userAuth.getIsAgreement());
        }
    }

    @Nested
    class changePassword {

        @DisplayName("성공 - 새로운_비밀번호를_가진_UserAuth_객체_생성")
        @Test
        void test1000() {
            //given
            String newEncodedPassword = "newEncodedPassword";
            UserAuth userAuth = UserAuth.builder()
                .id(1L)
                .username("userA@email.com")
                .password(newEncodedPassword)
                .role(UserRole.USER)
                .isAgreement(true)
                .build();

            //when
            userAuth = userAuth.withPassword(newEncodedPassword);

            //then
            assertEquals(newEncodedPassword, userAuth.getPassword());
        }
    }
}