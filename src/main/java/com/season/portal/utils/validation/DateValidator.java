package com.season.portal.utils.validation;

import com.season.portal.utils.validation.constrain.IDateValidatorConstrain;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Date;

import static com.season.portal.utils.Utils.strToDate;

public class DateValidator implements ConstraintValidator<IDateValidatorConstrain, String> {
    private boolean required;
    @Override
    public void initialize(IDateValidatorConstrain constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        if(!validateEmpty(s, context))
            return false;

        if(s==null || s.isEmpty())
            return true;

        return canBeParsed(s, context);
    }

    public boolean validateEmpty(String s, ConstraintValidatorContext context) {
        boolean valid = true;

        if (required && (s == null ||s.isEmpty())){
            context.buildConstraintViolationWithTemplate(
                    "utils_form_required").addConstraintViolation();
            valid = false;
        }
        return valid;
    }

    private boolean canBeParsed(String s, ConstraintValidatorContext context) {
        boolean valid = true;
        Date result = strToDate(s);

        if(result == null) {
            context.buildConstraintViolationWithTemplate(
                    "utils_form_date_invalid").addConstraintViolation();
            valid = false;
        }

        return valid;
    }

}
