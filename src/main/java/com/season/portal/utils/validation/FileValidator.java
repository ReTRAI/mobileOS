package com.season.portal.utils.validation;

import com.season.portal.utils.validation.constrain.IFileValidatorConstrain;
import com.season.portal.utils.validation.constrain.IPasswordValidatorConstrain;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileValidator extends FileValidatorUtils implements ConstraintValidator<IFileValidatorConstrain, MultipartFile> {

    private boolean required;
    private String[] fileTypes;
    private long maxSizeMB;

    @Override
    public void initialize(IFileValidatorConstrain constraintAnnotation) {
        required = constraintAnnotation.required();
        fileTypes = constraintAnnotation.fileTypes();
        maxSizeMB = calcMaxMBSize(constraintAnnotation.maxSizeMB());
        fileTypes = groupsToFileTypes(constraintAnnotation.fileTypeGroups(), fileTypes);
    }

    @Override
    public boolean isValid(MultipartFile f, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        if(!validateEmpty(f, context))
            return false;

        //If not required no further validation
        if(f == null || f.isEmpty())
            return true;
        
        return validateFileSize(f, context, maxSizeMB) &&
               validateFileType(f, context, fileTypes);
    }

    public boolean validateEmpty(MultipartFile f, ConstraintValidatorContext context) {
        boolean valid = true;
        
        if (required && (f == null || f.isEmpty())){
            context.buildConstraintViolationWithTemplate(
                    "utils_form_required").addConstraintViolation();
            valid = false;
        }
        return valid;
    }

}
