package site.travellaboratory.be.common.exceptionhandler;

import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.common.response.ApiErrorResponse;

@Profile("prod")
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = { BeApplicationException.class })
    public ResponseEntity<ApiErrorResponse> beApplicationExceptionHandler(
        BeApplicationException exception) {
        log.error("Error occurs {}", exception.toString());

        final ApiErrorResponse body = ApiErrorResponse.from(exception.getErrorCodes());
        final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);
        final HttpStatus status = exception.getStatus();

        return ResponseEntity.status(status)
            .contentType(contentType)
            .body(body);
    }

    @ExceptionHandler({
//        MissingServletRequestParameterException.class,
//        MethodArgumentNotValidException.class,
//        HttpMessageNotReadableException.class,
        MissingRequestCookieException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponse> handleBadRequestExceptions(Exception ex) {
        log.error("Bad Request Exception: {}", ex.getMessage());

        final ApiErrorResponse body = ApiErrorResponse.from(ErrorCodes.BAD_REQUEST);
        final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);
        final HttpStatus status = HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status)
            .contentType(contentType)
            .body(body);
    }

    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<ApiErrorResponse> RuntimeExceptionHandler(
        RuntimeException exception) {
        log.error("Internal Server Error occurs - {}", exception.getStackTrace()[0]);

        final ApiErrorResponse body = ApiErrorResponse.from(ErrorCodes.INTERNAL_SERVER_ERROR);
        final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);
        final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseEntity.status(status)
            .contentType(contentType)
            .body(body);
    }
}
