package it.laskaridis.blueground.commons.validation.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @see TimeZoneValidator
 */

@Documented
@Constraint(validatedBy = TimeZoneValidator.class)
@Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Repeatable(TimeZone.List.class)
public @interface TimeZone {

    String message() default "Invalid time zone. Legal examples: 'Europe/Athens', etc";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };

    @Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        TimeZone[] value();
    }

}
