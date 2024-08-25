package site.travellaboratory.be.common.presentation.exceptionhandler;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.travellaboratory.be.common.error.ErrorCodes;
import site.travellaboratory.be.common.presentation.response.ApiResponse;

@Profile("dev")
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleJsonParseExceptionHandler(HttpMessageNotReadableException ex) {
        log.info("Json Parse failed: {}", ex.getMessage());

        final ApiResponse<Object> body = ApiResponse.ERROR(ErrorCodes.BAD_REQUEST_JSON_PARSE_ERROR);
        final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .contentType(contentType)
            .body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidExceptionHandler(MethodArgumentNotValidException ex) {
        log.info("Validation failed: {}", ex.getMessage());

        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
            .collect(Collectors.toMap(
                fieldError -> camelToSnake(fieldError.getField()),
                FieldError::getDefaultMessage
            ));

        final ApiResponse<Object> body = ApiResponse.ERROR(ErrorCodes.BAD_REQUEST, errors);
        final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .contentType(contentType)
            .body(body);
    }

    @ExceptionHandler(value = { RuntimeException.class })
    public ResponseEntity<ApiResponse<Object>> RuntimeExceptionHandler(
        RuntimeException ex) {
        log.error("Internal Server Error occurs - {}", ex.getStackTrace()[0]);


        final ApiResponse<Object> body = ApiResponse.ERROR(ErrorCodes.INTERNAL_SERVER_ERROR);
        final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);
        final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseEntity.status(status)
            .contentType(contentType)
            .body(body);
    }

    private String camelToSnake(String str) {
        return str.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }
}
