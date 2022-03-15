package com.season.portal.utils.validation;

import com.season.portal.PortalApplication;
import com.season.portal.utils.validation.constrain.IFileValidatorConstrain;
import com.season.portal.utils.validation.constrain.IGuidValidatorConstrain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;
import java.util.regex.Pattern;

public class GuidValidator implements ConstraintValidator<IGuidValidatorConstrain, String> {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private boolean required;
    public final static Pattern UUID_REGEX_PATTERN =
            Pattern.compile("^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$");

    @Override
    public void initialize(IGuidValidatorConstrain constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        if(!validateEmpty(s, context))
            return false;

        //If not required no further validation
        if(s == null || s.isEmpty())
            return true;
        
        return isGuid(s, context);
    }

    private boolean isGuid(String s, ConstraintValidatorContext context) {
        boolean valid = true;

        if(!UUID_REGEX_PATTERN.matcher(s).matches()){
            valid = false;
            context.buildConstraintViolationWithTemplate(
                    "utils_form_guid_invalid").addConstraintViolation();
        }
        return valid;
    }

    public boolean validateEmpty(String s, ConstraintValidatorContext context) {
        boolean valid = true;
        
        if (required && (s == null || s.isEmpty())){
            context.buildConstraintViolationWithTemplate(
                    "utils_form_required").addConstraintViolation();
            valid = false;
        }
        return valid;
    }

}
