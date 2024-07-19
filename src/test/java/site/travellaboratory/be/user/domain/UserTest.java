package site.travellaboratory.be.user.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import site.travellaboratory.be.user.domain.enums.UserStatus;

class UserTest {

    @Nested
    class registerUser {

        @DisplayName("성공 - User 객체 등록")
        @Test
        void test1000() {
            //given
            String nickname = "newUser";

            //when
            User user = User.register(nickname);

            //then
            assertNotNull(user);
            assertNull(user.getId());  // ID는 설정되지 않음
            assertEquals(nickname, user.getNickname());
            assertNull(user.getProfileImgUrl());  // Profile 이미지 URL은 설정되지 않음
            assertNull(user.getIntroduce());  // 소개는 설정되지 않음
            assertEquals(UserStatus.ACTIVE, user.getStatus());
        }
    }
}