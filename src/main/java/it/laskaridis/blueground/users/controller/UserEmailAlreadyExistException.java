package it.laskaridis.blueground.users.controller;

import it.laskaridis.blueground.errors.model.ApplicationException;

import static java.lang.String.format;

public class UserEmailAlreadyExistException extends ApplicationException {

    public UserEmailAlreadyExistException(String email) {
        super("UserEmailTaken", format("user email `%s` has been taken", email), NON_RETRYABLE);
    }
}
