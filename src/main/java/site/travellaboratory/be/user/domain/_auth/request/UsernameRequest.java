package site.travellaboratory.be.user.domain._auth.request;

import lombok.Builder;

public record UsernameRequest(
    String username
) {
    @Builder
    public UsernameRequest {
    }
}
