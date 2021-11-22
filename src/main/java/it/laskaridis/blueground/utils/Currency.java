package it.laskaridis.blueground.utils;

import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Objects;

public final class Currency implements Serializable {

    public static final Currency DEFAULT = new Currency("EUR");

    private final String code;

    public Currency(String code) {
        Assert.notNull(code, "code can't be null");
        this.code = code;
    }

    public static Currency of(String currency) {
        return new Currency(currency);
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return code.equals(currency.code);
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
