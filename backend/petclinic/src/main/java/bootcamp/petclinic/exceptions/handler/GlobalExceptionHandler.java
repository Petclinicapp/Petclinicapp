package bootcamp.petclinic.exceptions.handler;

import bootcamp.petclinic.exceptions.*;
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

    @ExceptionHandler(PetNotFoundException.class)
    public ResponseEntity<ErrorDetails> exceptionPetNotFoundHandler(PetNotFoundException ex) {
        ErrorDetails errorDetails = new ErrorDetails(ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(VisitNotFoundException.class)
    public ResponseEntity<ErrorDetails> exceptionVisitNotFoundHandler(VisitNotFoundException ex) {
        ErrorDetails errorDetails = new ErrorDetails(ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ErrorDetails> exceptionUnauthorizedAccessHandler(UnauthorizedAccessException ex) {
        ErrorDetails errorDetails = new ErrorDetails(ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }
}
