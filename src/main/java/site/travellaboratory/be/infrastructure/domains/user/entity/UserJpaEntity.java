package site.travellaboratory.be.infrastructure.domains.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.travellaboratory.be.domain.user.user.User;
import site.travellaboratory.be.domain.user.auth.UserAuth;
import site.travellaboratory.be.infrastructure.common.BaseEntity;
import site.travellaboratory.be.domain.user.auth.enums.UserRole;
import site.travellaboratory.be.domain.user.enums.UserStatus;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String nickname;

    private String profileImgUrl;

    private String introduce;

    private Boolean isAgreement;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @PrePersist
    void prePersist() {
        this.role = UserRole.USER;
        this.status = UserStatus.ACTIVE;
    }

    public static UserJpaEntity from(User user, UserAuth userAuth) {
        UserJpaEntity result = new UserJpaEntity();
        result.id = user.getId();
        result.username = userAuth.getUsername();
        result.password = userAuth.getPassword();
        result.role = userAuth.getRole();
        result.nickname = user.getNickname();
        result.profileImgUrl = user.getProfileImgUrl();
        result.introduce = user.getIntroduce();
        result.isAgreement = userAuth.getIsAgreement();
        result.status = user.getStatus();
        return result;
    }

    public User toModel() {
        return User.builder()
            .id(this.id)
            .nickname(this.nickname)
            .profileImgUrl(this.profileImgUrl)
            .introduce(this.introduce)
            .status(this.status)
            .build();
    }

    public UserAuth toModelUserAuth() {
        return UserAuth.builder()
            .id(this.id)
            .username(this.username)
            .password(this.password)
            .role(this.role)
            .isAgreement(this.isAgreement)
            .build();
    }

    public void update(final String nickname, final String profileImgUrl, final String introduce) {
        this.nickname = nickname;
        this.profileImgUrl = profileImgUrl;
        this.introduce = introduce;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public void setNickname(final String nickname) {
        this.nickname = nickname;
    }

    private void setUsername(final String username) {
        this.username = username;
    }

    private void setProfileImgUrl(final String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }

    public void setIsAgreement(Boolean agreement) {
        isAgreement = agreement;
    }

    // 일반 회원가입 시
    public static UserJpaEntity of(String username, String password, String nickname, Boolean isAgreement) {
        UserJpaEntity userJpaEntity = new UserJpaEntity();
        userJpaEntity.setUsername(username);
        userJpaEntity.setPassword(password);
        userJpaEntity.setNickname(nickname);
        userJpaEntity.setIsAgreement(isAgreement);
        return userJpaEntity;
    }

    // 소셜 로그인 시
    public static UserJpaEntity socialOf(String email, String profileImgUrl, String nickname, Boolean isAgreement) {
        UserJpaEntity userJpaEntity = new UserJpaEntity();
        userJpaEntity.setUsername(email);
        userJpaEntity.setProfileImgUrl(profileImgUrl);
        userJpaEntity.setNickname(nickname);
        userJpaEntity.setIsAgreement(isAgreement);
        return userJpaEntity;
    }

    // 후기 좋아요를 위한 생성자
    private UserJpaEntity(Long id) {
        this.id = id;
    }

    // 후기 좋아요를 위한 of
    public static UserJpaEntity of(Long userId) {
        return new UserJpaEntity(userId);
    }

    public void delete() {
        this.status = UserStatus.INACTIVE;
    }
}
