package site.travellaboratory.be.user.infrastructure.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static site.travellaboratory.be.test.assertion.Assertions.assertDataJpaTestUserEntityEquals;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import site.travellaboratory.be.common.presentation.config.JsonConfig;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain._auth.enums.UserRole;
import site.travellaboratory.be.user.domain.enums.UserStatus;
import site.travellaboratory.be.user.infrastructure.persistence.entity.UserEntity;

@Import({JsonConfig.class})
@ActiveProfiles("h2-test")
@DataJpaTest
class UserJpaRepositoryTest {

    @Autowired
    private UserJpaRepository userJpaRepository;

    private UserEntity savedUserEntity;
    private final LocalDateTime currentTime = LocalDateTime.now();

    @BeforeEach
    void setUp() {


        this.savedUserEntity = UserEntity.from(User.builder()
            .username("userA@example.com")
            .password("password")
            .role(UserRole.USER)
            .nickname("userA")
            .profileImgUrl("profileImgUrl")
            .introduce("introduce")
            .isAgreement(true)
            .status(UserStatus.ACTIVE)
            .createdAt(currentTime)
            .updatedAt(currentTime)
            .build());
        userJpaRepository.saveAndFlush(this.savedUserEntity);
    }

    @Nested
    @DisplayName("USER ID와 상태로 유저 조회")
    class FindByIdAndStatus {

        @DisplayName("성공 - 유저 조회")
        @Test
        void test1000() {
            //given
            Long userId = savedUserEntity.getId();

            // when
            Optional<UserEntity> userEntityOptional = userJpaRepository.findByIdAndStatus(userId, UserStatus.ACTIVE);

            // then
            assertThat(userEntityOptional).isPresent();
            UserEntity result = userEntityOptional.get();
            assertDataJpaTestUserEntityEquals(result, savedUserEntity);
        }
    }

    @Nested
    @DisplayName("유저의 유저네임(이메일)과 상태로 유저 조회")
    class FindByUsernameAndStatusOrderByIdDesc {

        @DisplayName("성공 - 유저 조회")
        @Test
        void test1000() {
            //given
            String savedUserEntityUsername = savedUserEntity.getUsername();

            // when
            Optional<UserEntity> userEntityOptional = userJpaRepository.findByUsernameAndStatusOrderByIdDesc(savedUserEntityUsername, UserStatus.ACTIVE);

            // then
            assertThat(userEntityOptional).isPresent();
            UserEntity result = userEntityOptional.get();
            assertDataJpaTestUserEntityEquals(result, savedUserEntity);
        }
    }

    @Nested
    @DisplayName("유저의 닉네임으로 유저 조회")
    class FindByNickname {

        @DisplayName("성공 - 유저 조회")
        @Test
        void findByNickname() {
            // when
            Optional<UserEntity> userEntityOptional = userJpaRepository.findByNickname("userA");

            // then
            assertThat(userEntityOptional).isPresent();
            UserEntity result = userEntityOptional.get();
            assertDataJpaTestUserEntityEquals(result, savedUserEntity);
        }
    }
}
