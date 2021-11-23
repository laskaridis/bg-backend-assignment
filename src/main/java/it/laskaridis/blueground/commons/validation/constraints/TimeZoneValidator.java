package it.laskaridis.blueground.commons.validation.constraints;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.ZoneId;
import java.util.Optional;

/**
 * Validates that a given {{@link String}} representing a time-zone, is any of the
 * available time zone identifiers, as returned by {@link ZoneId#getAvailableZoneIds()}.
 * To use this validator against an {@link javax.persistence.Entity} bean's property,
 * annotate it with the {@link TimeZone}  annotation.
 *
 * @see TimeZone
 * @see ZoneId
 */
public class TimeZoneValidator implements ConstraintValidator<TimeZone, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext ctxt) {
        if (StringUtils.isEmpty(value))
            return true;
        Optional<String> zone = ZoneId.getAvailableZoneIds()
                .stream()
                .map(String::toLowerCase)
                .filter(z -> value.toLowerCase().equals(z))
                .findFirst();
        return zone.isPresent();
    }
}
