package kalah.game.errorHandling.exceptions;

import kalah.game.errorHandling.ApiError;
import org.springframework.http.HttpStatus;

public class ApiExceptionBase extends RuntimeException {

    private String error;
    private HttpStatus httpStatus;

    public ApiExceptionBase(String message, String error, HttpStatus httpStatus) {
        super(message);
        this.error = error;
        this.httpStatus = httpStatus;
    }

    public ApiError toError() {
        return new ApiError(super.getMessage(), error);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
