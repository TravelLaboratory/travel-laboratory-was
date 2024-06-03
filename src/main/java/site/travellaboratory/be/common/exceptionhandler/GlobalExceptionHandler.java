package site.travellaboratory.be.common.exceptionhandler;

import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {BeApplicationException.class})
    public ResponseEntity<ApiResponse<Void>> beApplicationExceptionHandler(
        BeApplicationException exception) {
        final ApiResponse<Void> body = ApiResponse.fromErrorCodes(exception.getErrorCodes());
        final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);
        final HttpStatus status = exception.getStatus();

        return ResponseEntity.status(status)
            .contentType(contentType)
            .body(body);
    }
}
