package kalah.game.errorHandling;

import kalah.game.errorHandling.exceptions.GameNotFoundException;
import kalah.game.errorHandling.exceptions.InvalidMoveException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = GameNotFoundException.class)
    public ResponseEntity<ApiError> handleGameNotFoundException(GameNotFoundException exception) {
        return ResponseEntity.status(exception.getHttpStatus()).body(exception.toError());
    }

    @ExceptionHandler(value = InvalidMoveException.class)
    public ResponseEntity<ApiError> handleInvalidMoveException(InvalidMoveException exception) {
        return ResponseEntity.status(exception.getHttpStatus()).body(exception.toError());
    }
}
