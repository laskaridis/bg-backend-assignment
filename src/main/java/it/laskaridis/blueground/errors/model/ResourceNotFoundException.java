package it.laskaridis.blueground.errors.model;

import static java.lang.String.format;

/**
 * Thrown after requests that reference required resources which don't exist.
 */
public class ResourceNotFoundException extends ApplicationException {

    public ResourceNotFoundException(String entity, String description) {
        super(format("NotFound.%s", entity), description, NON_RETRYABLE);
    }
}
