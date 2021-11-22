package it.laskaridis.blueground;

import it.laskaridis.blueground.common.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static java.util.stream.Collectors.toList;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({ NotFoundException.class })
    public ResponseEntity handleNotFoundException(NotFoundException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ClientError.from(e));
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity handleConstraintViolationException(ConstraintViolationException e, WebRequest request) {
        List<ClientValidationError> errors = e.getConstraintViolations()
                .stream()
                .map(ClientValidationError::toError)
                .collect(toList());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errors);
    }
}
