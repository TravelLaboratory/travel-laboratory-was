package site.travellaboratory.be.user.application._auth;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain._auth.UserAuth;
import site.travellaboratory.be.user.domain._auth.request.UserNicknameRequest;
import site.travellaboratory.be.user.domain._auth.request.UsernameRequest;
import site.travellaboratory.be.user.domain.enums.UserStatus;
import site.travellaboratory.be.user.infrastructure.persistence.entity.UserEntity;
import site.travellaboratory.be.user.infrastructure.persistence.repository.UserJpaRepository;

@ExtendWith(MockitoExtension.class)
class UserVerificationServiceTest {

    @InjectMocks
    private UserVerificationService sut;

    @Mock
    private UserJpaRepository userJpaRepository;

    @Nested
    class isNicknameAvailable {
        @DisplayName("이미_존재하는_nickname_인_경우")
        @Test
        void test1() {
            //given
            String existingNickname = "existing_nickname";
            UserEntity userEntity = UserEntity.from(User.builder().nickname(existingNickname).build());
            UserNicknameRequest invalidRequest = UserNicknameRequest.builder().nickname(existingNickname).build();
            when(userJpaRepository.findByNickname(existingNickname)).thenReturn(Optional.of(userEntity));

            //when & then
            assertThrows(BeApplicationException.class, () ->
                sut.isNicknameAvailable(invalidRequest), ErrorCodes.AUTH_DUPLICATED_NICK_NAME.getMessage()
            );
        }

        @DisplayName("성공 - 존재하지_않는_nickname_인_경우")
        @Test
        void test1000() {
            //given
            String newNickname = "new_nickname";
            UserNicknameRequest validRequest = UserNicknameRequest.builder().nickname(newNickname).build();
            when(userJpaRepository.findByNickname(newNickname)).thenReturn(Optional.empty());

            //when & then
            Boolean result = sut.isNicknameAvailable(validRequest);
            assertTrue(result);
        }
    }

    @Nested
    class isUsernameAvailable {
        @DisplayName("이미_존재하는_username_인_경우")
        @Test
        void test1() {
            //given
            String existingUsername = "existing_username";
            UserEntity userEntity = UserEntity.from(User.builder().build(), UserAuth.builder().username(existingUsername).build());
            UsernameRequest invalidRequest = UsernameRequest.builder().username(existingUsername).build();
            when(userJpaRepository.findByUsernameAndStatusOrderByIdDesc(existingUsername, UserStatus.ACTIVE))
                .thenReturn(Optional.of(userEntity));

            //when & then
            assertThrows(BeApplicationException.class, () ->
                sut.isUsernameAvailable(invalidRequest), ErrorCodes.AUTH_DUPLICATED_NICK_NAME.getMessage()
            );
        }

        @DisplayName("성공 - 존재하지_않는_username_인_경우")
        @Test
        void test1000() {
            //given
            String newUserName = "new_username";
            UsernameRequest validRequest = UsernameRequest.builder().username(newUserName).build();
            when(userJpaRepository.findByUsernameAndStatusOrderByIdDesc(newUserName, UserStatus.ACTIVE)).thenReturn(Optional.empty());

            //when & then
            Boolean result = sut.isUsernameAvailable(validRequest);
            assertTrue(result);
        }
    }
}