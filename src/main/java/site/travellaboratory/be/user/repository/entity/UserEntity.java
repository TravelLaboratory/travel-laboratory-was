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
import org.hibernate.annotations.SQLDelete;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Entity
@Table(name = "user")
@Getter
@SQLDelete(sql = "UPDATE user SET delete_at = NOW() where id = ?")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @NotNull Long id;

    private @NotNull String userName;

    private @NotNull String password;

    @Enumerated(EnumType.STRING)
    private @NotNull UserRole role;

    private @NotNull String nickName;

    private @Nullable String profileImgUrl;

    private @NotNull Timestamp registerAt;

    private @Nullable Timestamp updateAt;

    private @Nullable Timestamp deleteAt;

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
