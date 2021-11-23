package it.laskaridis.blueground.errors.controller;

import it.laskaridis.blueground.errors.model.ResourceNotFoundException;
import it.laskaridis.blueground.errors.view.ClientErrorView;
import it.laskaridis.blueground.errors.view.ClientValidationErrorView;
import it.laskaridis.blueground.users.controller.UserEmailAlreadyExistException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({ ResourceNotFoundException.class })
    public ResponseEntity handleNotFoundException(ResourceNotFoundException e, WebRequest request) {
        return ResponseEntity.status(NOT_FOUND).body(ClientErrorView.from(e));
    }

    @ExceptionHandler({UserEmailAlreadyExistException.class })
    public ResponseEntity handleNotFoundException(UserEmailAlreadyExistException e, WebRequest request) {
        return ResponseEntity.status(UNPROCESSABLE_ENTITY).body(ClientErrorView.from(e));
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity handleConstraintViolationException(ConstraintViolationException e, WebRequest request) {
        List<ClientValidationErrorView> errors = e.getConstraintViolations()
                .stream()
                .map(ClientValidationErrorView::from)
                .collect(toList());
        return ResponseEntity.status(UNPROCESSABLE_ENTITY).body(errors);
    }

    @ExceptionHandler({ UnsupportedOperationException.class })
    public ResponseEntity handleUnsupportedOperatorException(UnsupportedOperationException e, WebRequest request) {
        return ResponseEntity.status(NOT_IMPLEMENTED).build();
    }
}
