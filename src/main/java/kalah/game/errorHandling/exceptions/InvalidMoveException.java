package kalah.game.errorHandling.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidMoveException extends ApiExceptionBase {
    public InvalidMoveException(String message, String error) {
        super(message, error, HttpStatus.BAD_REQUEST);
    }
}
