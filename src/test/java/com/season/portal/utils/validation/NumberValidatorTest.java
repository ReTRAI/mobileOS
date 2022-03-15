package com.season.portal.utils.validation;

import com.season.portal.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import javax.validation.ConstraintValidatorContext;

import static org.assertj.core.api.Assertions.assertThat;

class NumberValidatorTest {

    private NumberValidator validator;
    private ConstraintValidatorContext context;
    private ConstraintValidatorContext.ConstraintViolationBuilder builder;
    private boolean result = false;

    @BeforeEach
    public void setup() {
        validator =  new NumberValidator();

        // mock the context
        context = Mockito.mock(ConstraintValidatorContext.class);
        context.disableDefaultConstraintViolation();
        // context.buildConstraintViolationWithTemplate returns
        // ConstraintValidatorContext.ConstraintViolationBuilder
        // so we mock that too as you will be calling one of it's methods
        builder = Mockito.mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);

        // when the context.buildConstraintViolationWithTemplate is called,
        // the mock should return the builder.
        Mockito.when(context.buildConstraintViolationWithTemplate(Mockito.anyString()))
                .thenReturn(builder);
    }

    @ParameterizedTest
    @DisplayName("Should validate numbers")
    @CsvSource({
            "null, false",
            "1, true",
            "102, true",
            "102., true",
            "., false",
            "+., false",
            "-., false",
            "+1., true",
            "-1., true",
            "1., true",
            "1.165, true",
            ".245, true",
            "+.245, true",
            "-.245, true",
            "0.654, true",
            "qq*q, false",
            "aaaa1111, false",
            "a1, false",
            "1a, false",
    })
    void isNumber(String number, String expected) {
        result = validator.isNum(number, context);
        assertThat(result).isEqualTo(Boolean.valueOf(expected));
    }

    @ParameterizedTest
    @DisplayName("Should parse numbers")
    @CsvSource({
            "null, false",
            "1, true",
            "102, true",
            "102., true",
            "., false",
            "+., false",
            "-., false",
            "+1., true",
            "-1., true",
            "1., true",
            "-1.., false",
            "-.1., false",
            "1.0.0, false",
            "1.165, true",
            ".245, true",
            "+.245, true",
            "-.245, true",
            "0.654, true",
            "qq*q, false",
            "aaaa1111, false",
            "a1, false",
            "1a, false",
    })
    void parseNumber(String number, String expected) {
        result = parseFloatResult(number);
        assertThat(result).isEqualTo(Boolean.valueOf(expected));
    }

    @Test
    @DisplayName("Should parse numbers with ,")
    void parseNumber() {
        result = parseFloatResult("1,3");
        assertThat(result).isEqualTo(Boolean.valueOf(true));

        result = parseFloatResult(",3");
        assertThat(result).isEqualTo(Boolean.valueOf(true));

        result = parseFloatResult("-,3");
        assertThat(result).isEqualTo(Boolean.valueOf(true));

        result = parseFloatResult("+,3");
        assertThat(result).isEqualTo(Boolean.valueOf(true));

        result = parseFloatResult("1,");
        assertThat(result).isEqualTo(Boolean.valueOf(true));


        result = parseFloatResult(",");
        assertThat(result).isEqualTo(Boolean.valueOf(false));
    }

    private boolean parseFloatResult(String numberStr){
        boolean r = true;
        numberStr = numberStr.replace(",", ".");
        try{
            float f = Float.parseFloat(numberStr);
            r= true;
        }catch(Exception e){
            r = false;
        }
        return r;
    }

    @ParameterizedTest
    @DisplayName("Should validate numbers with predefined number of places")
    @CsvSource({
            "5, 18, 4, true",
            "-5, 18, 0, true",
            "+5, 18, 0, true",
            "52354, 3, 4, false",
            "5, 0, 4, false",
            "5.33, 0, 4, false",
            "5.33, 1, 1, false",
            "5.3, 1, 1, true",
            "5.4, 18, 4, true",
            "-5.4, 18, 4, true",
            "+5.4, 18, 4, true",
            "-.54, 18, 4, true",
            "+.54, 18, 4, true",
            "-0.54, 18, 4, true",
            "+0.54, 18, 4, true",
            "+0.54, 18, 1, false",
    })
    void validateHouses(String number, int whole, int decimal, String expected){
        result = validator.validateHouses(number, context, whole, decimal);
        assertThat(result).isEqualTo(Boolean.valueOf(expected));
    }
}