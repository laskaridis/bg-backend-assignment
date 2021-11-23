package it.laskaridis.blueground.units.model;

import it.laskaridis.blueground.errors.model.ApplicationException;

public class ImageStorageException extends ApplicationException {

    public ImageStorageException(String s) {
        super("ImageStorage", s, RETRYABLE);
    }
}
