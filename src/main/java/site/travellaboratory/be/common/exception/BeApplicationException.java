package site.travellaboratory.be.common.exception;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

public class BeApplicationException extends RuntimeException {

    private @NotNull
    final ErrorCodes errorCodes;
    private @NotNull
    final HttpStatus status;


    public BeApplicationException(@NotNull ErrorCodes errorCodes, @NotNull HttpStatus status) {
        this.errorCodes = errorCodes;
        this.status = status;
    }

    public @NotNull ErrorCodes getErrorCodes() {
        return errorCodes;
    }

    public @NotNull HttpStatus getStatus() {
        return status;
    }
}