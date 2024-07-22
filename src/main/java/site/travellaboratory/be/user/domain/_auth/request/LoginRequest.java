package site.travellaboratory.be.user.domain._auth.request;

import lombok.Builder;

public record LoginRequest(
    String username,
    String password
) {
    @Builder
    public LoginRequest {
    }
}

