package it.laskaridis.blueground.common;

import org.hibernate.validator.constraints.Length;

import javax.persistence.Embeddable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public final class Money implements Serializable {

    public static final String DEFAULT_CURRENCY = "EUR";

    public static final Money of(BigDecimal amount) {
        return new Money(amount);
    }

    public static final Money of(BigDecimal amount, String currency) {
        return new Money(amount, currency);
    }

    @NotNull @Min(0)
    private final BigDecimal amount;

    @NotBlank @Length(min = 3, max = 3)
    private final String currency;

    public Money() {
        this(BigDecimal.ZERO);
    }

    public Money(BigDecimal amount) {
        this(amount, DEFAULT_CURRENCY);
    }

    public Money(BigDecimal amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount) && Objects.equals(currency, money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return this.amount.toPlainString() + " " + this.currency;
    }
}
