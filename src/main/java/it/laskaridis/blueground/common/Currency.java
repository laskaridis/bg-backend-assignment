package it.laskaridis.blueground.common;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

public final class Currency implements Serializable {

    public static final Currency DEFAULT = new Currency("EUR");

    public static Currency of(String currency) {
        return new Currency(currency);
    }

    @NotBlank
    private final String code;

    public Currency(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return Objects.equals(code, currency.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return this.code.toUpperCase();
    }
}
