package site.travellaboratory.be.common.response;

import java.util.Map;
import site.travellaboratory.be.common.exception.ErrorCodes;

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