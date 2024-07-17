package site.travellaboratory.be.user.presentation._auth.response.userauthentication;

public record LoginRequest(
    String username,
    String password
) {
}

