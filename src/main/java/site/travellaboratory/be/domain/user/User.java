package site.travellaboratory.be.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private String nickname;

    private String profileImgUrl;

    private String introduce;

    public User(Long id, String username) {
        this.id = id;
        this.username = username;
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
}
