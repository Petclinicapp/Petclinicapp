package bootcamp.petclinic.exceptions.handler;

import bootcamp.petclinic.exceptions.UserAlreadyExistsException;
import bootcamp.petclinic.exceptions.UserAlreadyLoggedInException;
import bootcamp.petclinic.exceptions.UserNotFoundException;
import bootcamp.petclinic.exceptions.UsernameOrPasswordInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorDetails> exceptionUserAlreadyExistsHandler(UserAlreadyExistsException ex) {
        ErrorDetails errorDetails = new ErrorDetails(ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserAlreadyLoggedInException.class)
    public ResponseEntity<ErrorDetails> exceptionUserAlreadyLoggedInHandler(UserAlreadyLoggedInException ex) {
        ErrorDetails errorDetails = new ErrorDetails(ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDetails> exceptionUserNotFoundHandler(UserNotFoundException ex) {
        ErrorDetails errorDetails = new ErrorDetails(ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameOrPasswordInvalidException.class)
    public ResponseEntity<ErrorDetails> exceptionUsernameOrPasswordInvalidHandler(UsernameOrPasswordInvalidException ex) {
        ErrorDetails errorDetails = new ErrorDetails(ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }
}
