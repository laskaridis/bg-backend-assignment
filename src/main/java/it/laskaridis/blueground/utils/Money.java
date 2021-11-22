package it.laskaridis.blueground.utils;

import org.springframework.util.Assert;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.math.BigDecimal;

@Embeddable
public final class Money implements Serializable {

    public static final Money of(BigDecimal amount) {
        return new Money(amount);
    }

    public static final Money of(BigDecimal amount, String currency) {
        return new Money(amount, Currency.of(currency));
    }

    private final BigDecimal amount;
    private final Currency currency;

    public Money() {
        this(BigDecimal.ZERO);
    }

    public Money(BigDecimal amount) {
        this(amount, Currency.DEFAULT);
    }

    public Money(BigDecimal amount, Currency currency) {
        Assert.notNull(amount, "amount can't be null");
        Assert.notNull(currency, "currency can't be null");
        this.amount = amount;
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }
}
