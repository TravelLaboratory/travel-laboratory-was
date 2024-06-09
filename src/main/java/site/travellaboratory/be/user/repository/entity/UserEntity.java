package site.travellaboratory.be.user.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.time.Instant;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Entity
@Table(name = "user")
@Getter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String nickName;

    private String profileImgUrl;

    private Timestamp registerAt;

    private Timestamp updateAt;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    public void setUserName(@NotNull String userName) {
        this.userName = userName;
    }

    public void setPassword(@NotNull String password) {
        this.password = password;
    }

    public void setNickName(@NotNull String nickName) {
        this.nickName = nickName;
    }


    @PrePersist
    void prePersist() {
        this.role = UserRole.USER;
        this.registerAt = Timestamp.from(Instant.now());
        this.status = UserStatus.ACTIVE;
    }

    @PreUpdate
    void updateAt() {
        this.updateAt = Timestamp.from(Instant.now());
    }

    public static UserEntity of(@NotNull String userName, @NotNull String password, @NotNull String nickName) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(userName);
        userEntity.setPassword(password);
        userEntity.setNickName(nickName);
        return userEntity;
    }
}
