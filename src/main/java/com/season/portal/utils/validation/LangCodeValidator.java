package com.season.portal.utils.validation;

import com.season.portal.utils.validation.constrain.ILangCodeValidatorConstrain;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class LangCodeValidator implements ConstraintValidator<ILangCodeValidatorConstrain, String> {
    public static String[] LANGUAGE_CODES = {"pt", "en"};
    @Override
    public void initialize(ILangCodeValidatorConstrain langCode) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        if(s == null)
            return false;

        context.disableDefaultConstraintViolation();

        return contains(s, context);
    }

    public boolean contains(String s, ConstraintValidatorContext context) {
        // Default validity is true until proven otherwise.
        boolean valid = true;

        if (!Arrays.asList(LANGUAGE_CODES).contains(s)) {
            valid = false;
            context.buildConstraintViolationWithTemplate(
                    "utils_form_langCode_invalid").addConstraintViolation();
        }

        return valid;
    }

}
