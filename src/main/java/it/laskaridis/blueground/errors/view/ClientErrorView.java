package it.laskaridis.blueground.errors.view;

import it.laskaridis.blueground.errors.model.ApplicationException;

/**
 * Renders a client view error
 */
public class ClientErrorView {

    /**
     * Factory method to create a {@link ClientErrorView} (view) instance from a corresponding
     * {@link ApplicationException} model.
     */
    public static ClientErrorView from(ApplicationException exception) {
        return new ClientErrorView(exception.getCode(), exception.getDescription(), exception.isRetryable());
    }

    private final String errorCode;
    private final String errorDescription;
    private final boolean retryable;

    public ClientErrorView(String code, String description, boolean retryable) {
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

    /**
     * @see ApplicationException#isRetryable()
     */
    public boolean isRetryable() {
        return retryable;
    }
}
