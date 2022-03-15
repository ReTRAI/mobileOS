
package com.season.portal.utils.validation.constrain;

import com.season.portal.utils.validation.DateValidator;
import com.season.portal.utils.validation.NumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NumberValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface INumberValidatorConstrain {
    String message() default "utils_form_number_invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    boolean required() default false;
    String minVal() default "";
    String maxVal() default "";
    int intHouses() default 16;
    int decimalHouses() default 4;
}
