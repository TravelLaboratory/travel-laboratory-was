package site.travellaboratory.be.user.controller.dto;

import org.jetbrains.annotations.NotNull;

public record UserJoinRequest(
    @NotNull String userName,
    @NotNull String password,
    @NotNull String nickName
) {
}

