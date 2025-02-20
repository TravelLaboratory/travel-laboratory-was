package site.travellaboratory.be.user.infrastructure.persistence.entity;

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
import site.travellaboratory.be.common.infrastructure.common.BaseEntity;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain._auth.enums.UserRole;
import site.travellaboratory.be.user.domain.enums.UserStatus;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseEntity {

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

    public static UserEntity from(User user) {
        UserEntity result = new UserEntity();
        result.id = user.getId();
        result.username = user.getUsername();
        result.password = user.getPassword();
        result.role = user.getRole();
        result.nickname = user.getNickname();
        result.profileImgUrl = user.getProfileImgUrl();
        result.introduce = user.getIntroduce();
        result.isAgreement = user.getIsAgreement();
        result.status = user.getStatus();
        result.setCreatedAt(user.getCreatedAt());
        result.setUpdatedAt(user.getUpdatedAt());
        return result;
    }

    public User toModel() {
        return User.builder()
            .id(this.id)
            .username(this.username)
            .password(this.password)
            .role(this.role)
            .nickname(this.nickname)
            .profileImgUrl(this.profileImgUrl)
            .introduce(this.introduce)
            .isAgreement(this.isAgreement)
            .status(this.status)
            .createdAt(this.getCreatedAt())
            .updatedAt(this.getUpdatedAt())
            .build();
    }

    // 소셜 로그인 시
    public static UserEntity socialOf(String email, String profileImgUrl, String nickname,
        Boolean isAgreement) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(email);
        userEntity.setProfileImgUrl(profileImgUrl);
        userEntity.setNickname(nickname);
        userEntity.setIsAgreement(isAgreement);
        return userEntity;
    }

    // 후기 좋아요를 위한 생성자
    private UserEntity(Long id) {
        this.id = id;
    }


    // 후기 좋아요를 위한 of
    public static UserEntity of(Long userId) {
        return new UserEntity(userId);
    }

    public void update(final String nickname, final String profileImgUrl, final String introduce) {
        this.nickname = nickname;
        this.profileImgUrl = profileImgUrl;
        this.introduce = introduce;
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

    public void delete() {
        this.status = UserStatus.INACTIVE;
    }
}
