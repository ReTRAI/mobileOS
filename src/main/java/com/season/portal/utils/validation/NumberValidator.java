package com.season.portal.utils.validation;

import com.season.portal.utils.Utils;
import com.season.portal.utils.validation.constrain.IFileValidatorConstrain;
import com.season.portal.utils.validation.constrain.INumberValidatorConstrain;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class NumberValidator implements ConstraintValidator<INumberValidatorConstrain, String> {

    private boolean required;
    private String minVal;
    private String maxVal;
    private float minNumVal;
    private float maxNumVal;
    private int intHouses;
    private int decimalHouses;
    public final static Pattern NUM_REGEX_PATTERN = Pattern.compile("^[+-]?((\\d+([,.]\\d*)?)|(\\d*([,.]\\d+)?))$");

    @Override
    public void initialize(INumberValidatorConstrain constraintAnnotation) {
        required = constraintAnnotation.required();
        minVal = constraintAnnotation.minVal();
        maxVal = constraintAnnotation.maxVal();
        minNumVal = (minVal.isEmpty())?0:Float.parseFloat(minVal);
        maxNumVal = (maxVal.isEmpty())?0:Float.parseFloat(maxVal);
        intHouses = constraintAnnotation.intHouses();
        decimalHouses = constraintAnnotation.decimalHouses();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        if(!validateEmpty(s, context))
            return false;

        //If not required no further validation
        if(s == null || s.isEmpty())
            return true;

        return  isNum(s, context) &&
                validateHouses(s, context, intHouses, decimalHouses) &&
                validateMinMax(s, context, minVal, maxVal, minNumVal, maxNumVal);//ultima validação para garantir que é um nr
    }

    public boolean validateHouses(String s, ConstraintValidatorContext context, int _intHouses, int _decimalHouses) {
        boolean valid = true;

        Pattern p = null;

        if(_intHouses == 0){
            p = Pattern.compile("^[+-]?[0]?([,.]\\d{0," + _decimalHouses + "})$");
        }
        else if(_decimalHouses == 0){
            p = Pattern.compile("^[-+]?(\\d{1," + _intHouses + "})$");
        }
        else{
            p = Pattern.compile("^[+-]?((\\d{1," + _intHouses + "}([,.]\\d{0," + _decimalHouses + "})?)|([,.]\\d{1," + _decimalHouses + "}))$");
        }

        if(!p.matcher(s).matches()){
            valid = false;
            context.buildConstraintViolationWithTemplate(
                    "utils_form_number_houses").addConstraintViolation();
        }
        return valid;
    }

    public boolean isNum(String s, ConstraintValidatorContext context) {
        boolean valid = true;

        if(!NUM_REGEX_PATTERN.matcher(s).matches()){
            valid = false;
            context.buildConstraintViolationWithTemplate(
                    "utils_form_number_invalid").addConstraintViolation();
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

    public boolean validateMinMax(String s, ConstraintValidatorContext context, String minVal, String maxVal, float minNumVal, float maxNumVal){
        boolean result = true;
        float num =  Utils.parseFloat(s);

        if(!minVal.isEmpty() && !maxVal.isEmpty()){
            if(num < minNumVal || num > maxNumVal){
                result = false;
                context.buildConstraintViolationWithTemplate(
                        "utils_form_number_minmaxNum").addConstraintViolation();
            }
        }
        else if (!minVal.isEmpty()){
            if(num < minNumVal){
                result = false;
                context.buildConstraintViolationWithTemplate(
                        "utils_form_number_minNum").addConstraintViolation();
            }
        }
        else if (!maxVal.isEmpty()){
            if(num > maxNumVal){
                result = false;
                context.buildConstraintViolationWithTemplate(
                        "utils_form_number_maxNum").addConstraintViolation();
            }
        }
        return result;
    }
}
