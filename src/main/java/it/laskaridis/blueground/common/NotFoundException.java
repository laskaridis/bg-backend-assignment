package it.laskaridis.blueground.common;

public class NotFoundException extends ApplicationException {
    public NotFoundException(String code, String description) {
        super(code, description, false);
    }
}
