package site.travellaboratory.be.user.service.domain;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.travellaboratory.be.user.repository.entity.UserRole;


@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private @NotNull Long id;
    private @NotNull String userName;
    private @NotNull String password;
    private @NotNull UserRole role;
    private @NotNull String nickName;
    private @Nullable String profileImgUrl;
    private @NotNull Timestamp registerAt;
    private @Nullable Timestamp updateAt;
    private @Nullable Timestamp deleteAt;
    private @Nullable String refreshToken;

    public @NotNull Long getId() {
        return id;
    }

    public @NotNull String getUserName() {
        return userName;
    }

    public @NotNull UserRole getRole() {
        return role;
    }

    public @NotNull String getNickName() {
        return nickName;
    }

    public @Nullable String getProfileImgUrl() {
        return profileImgUrl;
    }
}
