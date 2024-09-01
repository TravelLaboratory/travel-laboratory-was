package site.travellaboratory.be.common.presentation.response;

import java.util.Map;
import site.travellaboratory.be.common.presentation.error.ErrorCodes;

public record ApiResponse<T> (
    T data,
    ApiErrorResponse error
){
    public static <T> ApiResponse<T> OK(T data) {
        return new ApiResponse<>(data, null);
    }

    /*
    * Object - data: null로
    * Void - data 보이지 않게 되기에 의도와 다름
    * */
    public static <T> ApiResponse<T> ERROR(ErrorCodes errorCodes) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(errorCodes.message,
            errorCodes.code, null);
        return new ApiResponse<>(null, errorResponse);
    }

    public static <T> ApiResponse<T> ERROR(ErrorCodes errorCodes, Map<String, String> fieldErrors) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(errorCodes.message, errorCodes.code, fieldErrors);
        return new ApiResponse<>(null, errorResponse);
    }
}


