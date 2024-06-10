package site.travellaboratory.be.controller.auth.dto;

public record UserLoginRequest(
    String username,
    String password
) {
}

