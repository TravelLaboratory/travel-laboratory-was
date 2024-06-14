package site.travellaboratory.be.common.exceptionhandler;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.common.response.ApiErrorResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleJsonParseExceptionHandler(HttpMessageNotReadableException ex) {
        log.info("Json Parse failed: {}", ex.getMessage());

        final ApiErrorResponse body = ApiErrorResponse.from(ErrorCodes.BAD_REQUEST_JSON_PARSE_ERROR);
        final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .contentType(contentType)
            .body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidExceptionHandler(MethodArgumentNotValidException ex) {
        log.info("Validation failed: {}", ex.getMessage());

        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
            .collect(Collectors.toMap(
                fieldError -> StringUtil.camelToSnake(fieldError.getField()),
                FieldError::getDefaultMessage
            ));

        final ApiErrorResponse body = ApiErrorResponse.from(ErrorCodes.BAD_REQUEST, errors);
        final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .contentType(contentType)
            .body(body);
    }

    @ExceptionHandler(value = { BeApplicationException.class })
    public ResponseEntity<ApiErrorResponse> beApplicationExceptionHandler(
        BeApplicationException ex) {
        log.error("Error occurs {}", ex.toString());

        final ApiErrorResponse body = ApiErrorResponse.from(ex.getErrorCodes());
        final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);
        final HttpStatus status = ex.getStatus();

        return ResponseEntity.status(status)
            .contentType(contentType)
            .body(body);
    }

    @ExceptionHandler(value = { RuntimeException.class })
    public ResponseEntity<ApiErrorResponse> RuntimeExceptionHandler(
        RuntimeException ex) {
        log.error("Internal Server Error occurs - {}", ex.getStackTrace()[0]);

        final ApiErrorResponse body = ApiErrorResponse.from(ErrorCodes.INTERNAL_SERVER_ERROR);
        final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);
        final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseEntity.status(status)
            .contentType(contentType)
            .body(body);
    }
}
