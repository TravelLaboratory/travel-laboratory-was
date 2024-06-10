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

    @Enumerated(EnumType.STRING)
    private UserStatus status;


    public User(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    @PrePersist
    void prePersist() {
        this.role = UserRole.USER;
        this.status = UserStatus.ACTIVE;
    }

    public static User of(Long id, String username) {
        return new User(id, username);
    }

    public User(Long id, String nickname, String introduce) {
        this.id = id;
        this.nickname = nickname;
        this.introduce = introduce;
    }

    public User update(String nickname, String introduce) {
        return new User(this.id, nickname, introduce);
    }

    public User updateProfileImg(String profileImgUrl) {
        return new User(this.id, nickname, profileImgUrl);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public static User of(String username, String password, String nickname) {
        User userEntity = new User();
        userEntity.setUsername(username);
        userEntity.setPassword(password);
        userEntity.setNickname(nickname);
        return userEntity;
    }
}
