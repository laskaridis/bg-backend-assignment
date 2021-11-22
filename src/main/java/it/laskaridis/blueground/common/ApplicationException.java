package it.laskaridis.blueground.common;

public class ApplicationException extends RuntimeException {
    private final String code;
    private final String description;
    private final boolean retryable;

    public ApplicationException(String code, String description, boolean retryable) {
        this.code = code;
        this.description = description;
        this.retryable = retryable;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public boolean isRetryable() {
        return retryable;
    }
}
