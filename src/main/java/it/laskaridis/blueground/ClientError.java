package it.laskaridis.blueground;

import it.laskaridis.blueground.common.ApplicationException;

public class ClientError {

    public static ClientError from(ApplicationException exception) {
        return new ClientError(exception.getCode(), exception.getDescription(), exception.isRetryable());
    }

    private final String errorCode;
    private final String errorDescription;
    private final boolean retryable;

    public ClientError(String code, String description, boolean retryable) {
        this.errorCode = code;
        this.errorDescription = description;
        this.retryable = retryable;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public boolean isRetryable() {
        return retryable;
    }
}
