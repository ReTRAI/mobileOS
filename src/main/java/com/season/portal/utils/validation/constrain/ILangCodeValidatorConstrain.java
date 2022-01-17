package com.season.portal.utils.validation.constrain;

import com.season.portal.utils.validation.LangCodeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LangCodeValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ILangCodeValidatorConstrain {
    String message() default "utils_form_langCode_invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
