package site.travellaboratory.be.user.controller.dto;

import org.jetbrains.annotations.NotNull;

public record UserLoginRequest(
    @NotNull String userName,
    @NotNull String password
) {
}

