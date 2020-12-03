package kalah.game.errorHandling.exceptions;

import org.springframework.http.HttpStatus;

public class GameNotFoundException extends ApiExceptionBase {
    public GameNotFoundException(String message, String description) {
        super(message, description, HttpStatus.NOT_FOUND);
    }
}
