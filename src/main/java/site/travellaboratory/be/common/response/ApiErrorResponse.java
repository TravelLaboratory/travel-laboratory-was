package site.travellaboratory.be.common.response;

import jakarta.validation.constraints.NotNull;

public record ApiErrorResponse(
    @NotNull String localMessage,
    @NotNull Long code
) {
}