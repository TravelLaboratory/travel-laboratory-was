package site.travellaboratory.be.common.response;

import jakarta.validation.constraints.NotNull;
import site.travellaboratory.be.common.exception.ErrorCodes;

public record ApiErrorResponse(
    @NotNull String localMessage,
    @NotNull Long code
) {
    public static ApiErrorResponse from(ErrorCodes errorCodes) {
        return new ApiErrorResponse(errorCodes.message, errorCodes.code);
    }
}