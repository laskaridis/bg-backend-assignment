package it.laskaridis.blueground;

import org.springframework.util.Assert;

import javax.validation.ConstraintViolation;

public final class ClientValidationError extends ClientError {

    public static ClientValidationError toError(ConstraintViolation violation) {
        Assert.notNull(violation, "constraint violation can't be null");
        return new ClientValidationError(
                violation.getRootBeanClass().getSimpleName(),
                violation.getPropertyPath().toString(),
                violation.getMessage()
        );
    }

    private final String invalidEntity;
    private final String invalidField;
    private final String errorMessage;

    public ClientValidationError(String entity, String field, String message) {
        super(String.format("ValidationError.%s", entity), String.format("the field `%s` is invalid", field), false);
        this.invalidEntity = entity;
        this.invalidField = field;
        this.errorMessage = message;
    }

    public String getInvalidEntity() {
        return invalidEntity;
    }

    public String getInvalidField() {
        return invalidField;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
