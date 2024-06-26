package site.travellaboratory.be.controller.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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
}

