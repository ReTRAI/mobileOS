package com.season.portal.utils.validation;

import com.season.portal.utils.Utils;
import com.season.portal.utils.validation.constrain.IEnumValidatorConstrain;
import com.season.portal.utils.validation.constrain.IGuidValidatorConstrain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.regex.Pattern;

public class EnumValidator implements ConstraintValidator<IEnumValidatorConstrain, String> {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private boolean required;
    private String[] validValues;

    @Override
    public void initialize(IEnumValidatorConstrain constraintAnnotation) {
        required = constraintAnnotation.required();
        validValues = constraintAnnotation.enumValues();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        if(!validateEmpty(s, context))
            return false;

        //If not required no further validation
        if(s == null || s.isEmpty())
            return true;
        
        return isValidValue(s, context);
    }

    private boolean isValidValue(String s, ConstraintValidatorContext context) {
        boolean valid = true;

        if(!Arrays.asList(validValues).contains(s)){
            valid = false;
            context.buildConstraintViolationWithTemplate(
                    "utils_form_enum_invalidValue").addConstraintViolation();
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
