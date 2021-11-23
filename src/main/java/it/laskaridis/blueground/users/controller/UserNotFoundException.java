package it.laskaridis.blueground.users.controller;

import it.laskaridis.blueground.errors.model.ResourceNotFoundException;

import static java.lang.String.format;

public class UserNotFoundException extends ResourceNotFoundException {

    public UserNotFoundException(String uuid) {
        super("User", format("user `%s` does not exist", uuid));
    }
}
