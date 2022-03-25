package com.season.portal.utils.validation.constrain;

import com.season.portal.utils.validation.EnumValidator;
import com.season.portal.utils.validation.GuidValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.util.ArrayList;

@Documented
@Constraint(validatedBy = EnumValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface IEnumValidatorConstrain {
    String message() default "utils_form_guid_invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    boolean required() default true;
    String[] validValues();
}
