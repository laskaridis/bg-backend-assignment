package it.laskaridis.blueground.utils.jpa;

import it.laskaridis.blueground.utils.Currency;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class CurrencyConverter implements AttributeConverter<Currency, String> {
    @Override
    public String convertToDatabaseColumn(Currency currency) {
        return currency.getCode();
    }

    @Override
    public Currency convertToEntityAttribute(String s) {
        return new Currency(s);
    }
}
