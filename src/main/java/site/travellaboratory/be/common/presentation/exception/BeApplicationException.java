package site.travellaboratory.be.common.presentation.exception;

import org.springframework.http.HttpStatus;
import site.travellaboratory.be.common.presentation.error.ErrorCodes;

public class BeApplicationException extends RuntimeException {

    private final ErrorCodes errorCodes;
    private final HttpStatus status;


    public BeApplicationException(ErrorCodes errorCodes, HttpStatus status) {
        this.errorCodes = errorCodes;
        this.status = status;
    }

    public ErrorCodes getErrorCodes() {
        return errorCodes;
    }

    public HttpStatus getStatus() {
        return status;
    }
}