package kalah.game.errorHandling.exceptions;

import kalah.game.errorHandling.ApiError;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class ApiExceptionBaseTest {
    @Test
    void convertsExceptionDataIntoApiError() {
        DummyException exception = new DummyException("Something went wrong..", "randomError", HttpStatus.NOT_IMPLEMENTED);

        ApiError apiError = exception.toError();

        assertThat(apiError.getMessage()).isEqualTo(exception.getMessage());
        assertThat(apiError.getError()).isEqualTo(exception.getError());
    }

    private static class DummyException extends ApiExceptionBase {

        public DummyException(String message, String error, HttpStatus httpStatus) {
            super(message, error, httpStatus);
        }
    }
}
