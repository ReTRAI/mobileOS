package com.season.portal.utils.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface IPasswordValidatorConstrain {
    String message() default "utils_form_invalid_password";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
