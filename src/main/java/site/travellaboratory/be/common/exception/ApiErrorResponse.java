package site.travellaboratory.be.common.exception;

import java.util.Map;

public record ApiErrorResponse(
    String localMessage,
    Long code,
    Map<String, String> fieldErrors
) {
    public static ApiErrorResponse from(final ErrorCodes errorCodes) {
        return new ApiErrorResponse(errorCodes.message, errorCodes.code, null);
    }

    public static ApiErrorResponse from(final ErrorCodes errorCodes, Map<String, String> fieldErrors) {
        return new ApiErrorResponse(errorCodes.message, errorCodes.code, fieldErrors);
    }
}