package it.laskaridis.blueground.errors.view;

import org.springframework.util.Assert;

import javax.validation.ConstraintViolation;

public final class ClientValidationErrorView extends ClientErrorView {

    public static ClientValidationErrorView from(ConstraintViolation violation) {
        Assert.notNull(violation, "constraint violation can't be null");
        return new ClientValidationErrorView(
                violation.getRootBeanClass().getSimpleName(),
                violation.getPropertyPath().toString(),
                violation.getMessage()
        );
    }

    private final String invalidEntity;
    private final String invalidField;
    private final String errorMessage;

    public ClientValidationErrorView(String entity, String field, String message) {
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
