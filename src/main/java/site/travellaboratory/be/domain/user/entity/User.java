package site.travellaboratory.be.domain.user.entity;

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
import site.travellaboratory.be.domain.BaseEntity;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

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

//    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private PwAnswer pwAnswer;

    @PrePersist
    void prePersist() {
        this.role = UserRole.USER;
        this.status = UserStatus.ACTIVE;
    }

    // 프로필 변경
    public User(Long id, String username, String nickname, String profileImgUrl, String introduce) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.profileImgUrl = profileImgUrl;
        this.introduce = introduce;
        this.status = UserStatus.ACTIVE;
    }

    // 프로필 변경
    public User update(String username, String nickname, String profileImgUrl, String introduce) {
        return new User(this.id, username, nickname, profileImgUrl, introduce);
    }

//    public User updateProfileImg(String profileImgUrl) {
//        return new User(this.id, nickname, profileImgUrl);
//    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public void setNickname(final String nickname) {
        this.nickname = nickname;
    }

    private void setUsername(final String username) {
        this.username = username;
    }

    public void setIsAgreement(Boolean agreement) {
        isAgreement = agreement;
    }

    public static User of(String username, String password, String nickname, Boolean isAgreement) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setNickname(nickname);
        user.setIsAgreement(isAgreement);
        return user;
    }

    // 후기 좋아요를 위한 생성자
    private User(Long id) {
        this.id = id;
    }

    // 후기 좋아요를 위한 of
    public static User of(Long userId) {
        return new User(userId);
    }

}
