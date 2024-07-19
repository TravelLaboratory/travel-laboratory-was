package site.travellaboratory.be.user.domain._auth.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public record UserJoinRequest(
    @NotBlank
    String username,
    @NotBlank
    String password,
    @NotBlank
    String nickname,
    @NotNull
    Long pwQuestionId,
    @NotBlank
    String pwAnswer,
    @NotNull
    Boolean isAgreement
) {

    @Builder
    public UserJoinRequest {
    }
}
