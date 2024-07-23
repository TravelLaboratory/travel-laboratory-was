package site.travellaboratory.be.user.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.user.domain._auth.enums.UserRole;
import site.travellaboratory.be.user.domain._auth.request.UserJoinRequest;
import site.travellaboratory.be.user.domain.enums.UserStatus;

class UserTest {

    @Nested
    class register {

        @DisplayName("회원가입시_개인정보_미동의할_경우_예외_반환")
        @Test
        void test1() {
            //given
            String encodedPassword = "encoded_password";
            UserJoinRequest validRequest = UserJoinRequest.builder()
                .isAgreement(false)
                .build();

            //when
            BeApplicationException exception = assertThrows(BeApplicationException.class,
                () -> User.register(encodedPassword, validRequest));

            // then
            assertEquals(ErrorCodes.AUTH_USER_NOT_IS_AGREEMENT, exception.getErrorCodes());
        }

        @DisplayName("성공 - UserJoinRequest_으로_User_객체_생성")
        @Test
        void test1000() {
            //given
            String encodedPassword = "encoded_password";
            UserJoinRequest validRequest = UserJoinRequest.builder()
                .username("userA@email.com")
                .password("password")
                .nickname("newUser")
                .isAgreement(true)
                .build();

            //when
            User user = User.register(encodedPassword, validRequest);

            //then
            assertNotNull(user);
            assertNull(user.getId());  // ID는 설정되지 않음
            assertEquals(validRequest.username(), user.getUsername());
            assertEquals(UserRole.USER, user.getRole());
            assertEquals(validRequest.nickname(), user.getNickname());
            assertNull(user.getProfileImgUrl());  // Profile 이미지 URL은 설정되지 않음
            assertNull(user.getIntroduce());  // 소개는 설정되지 않음
            assertTrue(user.getIsAgreement());
            assertEquals(UserStatus.ACTIVE, user.getStatus());
        }
    }

    @Nested
    class withPassword {

        @DisplayName("성공 - 새로운_비밀번호를_가진_UserAuth_객체_생성")
        @Test
        void test1000() {
            //given
            String newEncodedPassword = "newEncodedPassword";
            User user = User.builder()
                .id(1L)
                .username("userA@email.com")
                .password(newEncodedPassword)
                .role(UserRole.USER)
                .nickname("userA")
                .profileImgUrl("http://userA_profile_image.png")
                .introduce("userA_introduce")
                .isAgreement(true)
                .status(UserStatus.ACTIVE)
                .build();

            //when
            User withPasswordUser = user.withPassword(newEncodedPassword);

            //then
            assertNotNull(withPasswordUser);
            assertEquals(user.getUsername(), withPasswordUser.getUsername());
            assertEquals(newEncodedPassword, withPasswordUser.getPassword());
            assertEquals(UserRole.USER, user.getRole());
            assertEquals(user.getNickname(), withPasswordUser.getNickname());
            assertEquals(user.getProfileImgUrl(), withPasswordUser.getProfileImgUrl());
            assertEquals(user.getIntroduce(), withPasswordUser.getIntroduce());
            assertTrue(user.getIsAgreement());
            assertEquals(UserStatus.ACTIVE, user.getStatus());
        }
    }

}
