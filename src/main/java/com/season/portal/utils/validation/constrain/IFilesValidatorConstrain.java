package com.season.portal.utils.validation.constrain;

import com.season.portal.utils.validation.FileValidator;
import com.season.portal.utils.validation.FilesValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FilesValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface IFilesValidatorConstrain {
    String message() default "utils_form_file_invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    boolean required() default true;
    String[] fileTypes() default {};

    long maxSizeMB() default -1;

    int maxFiles() default 5;

    String[] fileTypeGroups() default {};
}
