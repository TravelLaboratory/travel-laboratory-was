package site.travellaboratory.be.user.controller.dto;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.travellaboratory.be.user.repository.entity.UserRole;
import site.travellaboratory.be.user.service.domain.User;

public record UserJoinResponse(
    @NotNull Long id,
    @NotNull String userName,
    @NotNull UserRole role,
    @NotNull String nickName,
    @Nullable String profileImgUrl
) {
    public static UserJoinResponse fromDomain(@NotNull User user) {
        return new UserJoinResponse(
            user.getId(),
            user.getUserName(),
            user.getRole(),
            user.getNickName(),
            user.getProfileImgUrl()
        );
    }
}
