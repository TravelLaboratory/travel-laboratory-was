package site.travellaboratory.be.user.service.domain;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import site.travellaboratory.be.user.repository.entity.UserRole;


@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private Long id;
    private String userName;
    private String password;
    private UserRole role;
    private String nickName;
    private String profileImgUrl;
    private Timestamp registerAt;
    private Timestamp updateAt;
    private Timestamp deleteAt;
    private String refreshToken;

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public UserRole getRole() {
        return role;
    }

    public String getNickName() {
        return nickName;
    }

    public String getProfileImgUrl() {
        return profileImgUrl;
    }
}
