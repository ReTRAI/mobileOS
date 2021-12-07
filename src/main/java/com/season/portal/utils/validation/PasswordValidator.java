package com.season.portal.utils.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Properties;

public class PasswordValidator implements ConstraintValidator<IPasswordValidatorConstrain, String> {

    @Override
    public void initialize(IPasswordValidatorConstrain password) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        if(s == null)
            return false;

        context.disableDefaultConstraintViolation();

        return checkConstraint1(s, context) &&
               checkConstraint2(s, context);
    }

    private boolean checkConstraint1(String s, ConstraintValidatorContext context) {
        // Default validity is true until proven otherwise.
        boolean valid = true;

        if ((s.length() < 8) || (s.length() > 32)) {
            valid = false;
            context.buildConstraintViolationWithTemplate(
                    "<<Insert constraint #1 failure message here>>").addConstraintViolation();
        }

        return valid;
    }

    private boolean checkConstraint2(String s, ConstraintValidatorContext context) {
        // Default validity is true until proven otherwise.
        boolean valid = true;

        if ((s.length() < 8) || (s.length() > 32)) {
            valid = false;
            context.buildConstraintViolationWithTemplate(
                    "<<Insert constraint #1 failure message here>>").addConstraintViolation();
        }

        return valid;
    }
}
