package site.travellaboratory.be.user.domain._auth.request;

public record LoginRequest(
    String username,
    String password
) {
}

