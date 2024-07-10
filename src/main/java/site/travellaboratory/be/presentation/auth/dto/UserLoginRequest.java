package site.travellaboratory.be.presentation.auth.dto;

public record UserLoginRequest(
    String username,
    String password
) {
}

