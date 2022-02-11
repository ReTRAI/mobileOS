package com.season.portal.utils.validation.constrain;

import com.season.portal.utils.validation.FileValidator;
import com.season.portal.utils.validation.LangCodeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FileValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface IFileValidatorConstrain {
    String message() default "utils_form_file_invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    boolean canBeNull() default true;
    String[] fileTypes() default {};

    long maxSizeMB() default -1;

    String[] fileTypeGroups() default {};
}
