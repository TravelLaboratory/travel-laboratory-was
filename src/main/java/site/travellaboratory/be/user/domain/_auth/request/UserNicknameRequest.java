package site.travellaboratory.be.user.domain._auth.request;

import lombok.Builder;

public record UserNicknameRequest(
    String nickname
) {
    @Builder
    public UserNicknameRequest {
    }
}
