
package com.season.portal.utils.validation.constrain;

import com.season.portal.utils.validation.DateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface IDateValidatorConstrain {
    String message() default "utils_form_date_invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    boolean required() default false;
}
