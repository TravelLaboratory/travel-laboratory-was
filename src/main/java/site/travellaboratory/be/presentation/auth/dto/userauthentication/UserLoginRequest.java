package site.travellaboratory.be.presentation.auth.dto.userauthentication;

public record UserLoginRequest(
    String username,
    String password
) {
}

