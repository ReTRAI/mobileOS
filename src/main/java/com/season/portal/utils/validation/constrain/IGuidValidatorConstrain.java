package com.season.portal.utils.validation.constrain;

import com.season.portal.utils.validation.FileValidator;
import com.season.portal.utils.validation.GuidValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = GuidValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface IGuidValidatorConstrain {
    String message() default "utils_form_guid_invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    boolean required() default true;
}
