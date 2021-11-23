package it.laskaridis.blueground.commons.emails;

import it.laskaridis.blueground.errors.model.ApplicationException;

/**
 * Thrown from {@link ApplicationMailer}s when something goes wrong.
 */
public class EmailDispatchException extends ApplicationException {

    public EmailDispatchException(String code, String description) {
        super(code, description, false);
    }
}
