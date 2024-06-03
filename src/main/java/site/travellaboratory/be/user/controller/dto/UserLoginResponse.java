package site.travellaboratory.be.user.controller.dto;

import org.jetbrains.annotations.NotNull;

public record UserLoginResponse(
    @NotNull String token
) {

    public static UserLoginResponse fromDomain(@NotNull String token) {
        return new UserLoginResponse(
            token
        );
    }
}
