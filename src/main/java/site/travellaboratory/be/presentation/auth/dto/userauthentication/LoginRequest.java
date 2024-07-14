package site.travellaboratory.be.presentation.auth.dto.userauthentication;

public record LoginRequest(
    String username,
    String password
) {
}

