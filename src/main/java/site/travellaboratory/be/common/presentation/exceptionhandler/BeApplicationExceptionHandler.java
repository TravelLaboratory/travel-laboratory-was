package site.travellaboratory.be.common.presentation.exceptionhandler;

import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.travellaboratory.be.common.presentation.exception.BeApplicationException;
import site.travellaboratory.be.common.presentation.response.ApiResponse;

@Slf4j
@RestControllerAdvice
@Order(value = Integer.MIN_VALUE)
public class BeApplicationExceptionHandler {

    @ExceptionHandler(value = {BeApplicationException.class})
    public ResponseEntity<ApiResponse<Object>> beApplicationExceptionHandler(
        BeApplicationException ex) {
        log.error("Error occurs {}", ex.toString());

        final ApiResponse<Object> body = ApiResponse.ERROR(ex.getErrorCodes());
        final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON,
            StandardCharsets.UTF_8);
        final HttpStatus status = ex.getStatus();

        return ResponseEntity.status(status)
            .contentType(contentType)
            .body(body);
    }

}
