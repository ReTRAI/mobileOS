package com.season.portal.utils.validation;

import com.season.portal.utils.validation.constrain.IPasswordValidatorConstrain;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<IPasswordValidatorConstrain, String> {
    public static int PASS_MIN_CHAR = 8;
    public static int PASS_MAX_CHAR = 20;
    public static String PASS_SPECIAL_CHAR = "@#$%^&*-_+=[]{}|\\:‘,.?/`~“();";
    @Override
    public void initialize(IPasswordValidatorConstrain password) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        if(s == null)
            return false;

        context.disableDefaultConstraintViolation();

        return numChar(s, context) &&
                atLeastChars(s, context);
    }
/*
    public boolean invalidSequence(String s, ConstraintValidatorContext context) {
        // Default validity is true until proven otherwise.
        boolean valid = true;

        if ((s.length() < PASS_MIN_CHAR) || (s.length() > PASS_MAX_CHAR)) {
            valid = false;
            context.buildConstraintViolationWithTemplate(
                    "utils_form_password_sequence").addConstraintViolation();
        }

        return valid;
    }
*/
    public boolean numChar(String s, ConstraintValidatorContext context) {
        // Default validity is true until proven otherwise.
        boolean valid = true;

        if ((s.length() < PASS_MIN_CHAR) || (s.length() > PASS_MAX_CHAR)) {
            valid = false;
            context.buildConstraintViolationWithTemplate(
                    "utils_form_password_numChar").addConstraintViolation();
        }

        return valid;
    }



    public boolean atLeastChars(String s, ConstraintValidatorContext context) {
        // Default validity is true until proven otherwise.
        boolean valid = true;

        int countPass = 0;
        Matcher match = Pattern.compile("[a-z]").matcher(s);

        if (match.find()) {
            countPass++;
        }
        match = Pattern.compile("[A-Z]").matcher(s);
        if (match.find()) {
            countPass++;
        }
        match = Pattern.compile("[0-9]").matcher(s);
        if (match.find()) {
            countPass++;
        }

        String myRegexp = String.format("[%s]", Pattern.quote(PasswordValidator.PASS_SPECIAL_CHAR));
        match = Pattern.compile(myRegexp).matcher(s);
        if (match.find()) {
            countPass++;
        }

        if (countPass < 3) {
            valid = false;
            context.buildConstraintViolationWithTemplate(
                    "utils_form_password_atLeast").addConstraintViolation();
        }

        return valid;
    }


}
