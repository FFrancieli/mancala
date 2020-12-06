package kalah.game.errorHandling.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidPitIndexException extends ApiExceptionBase {
    public InvalidPitIndexException(String message, String error) {
        super(message, error, HttpStatus.BAD_REQUEST);
    }
}
