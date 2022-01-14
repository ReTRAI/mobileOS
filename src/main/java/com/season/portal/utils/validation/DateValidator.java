package com.season.portal.utils.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Date;

import static com.season.portal.utils.Utils.strToDate;

public class DateValidator implements ConstraintValidator<IDateValidatorConstrain, String> {
    @Override
    public void initialize(IDateValidatorConstrain date) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        if(s == null || s.equals(""))
            return true;
        //For multi validations with different messages
        //context.disableDefaultConstraintViolation();
        return canBeParsed(s, context);
    }

    private boolean canBeParsed(String s, ConstraintValidatorContext context) {
        Date result = strToDate(s);
        return result != null;
    }

}
