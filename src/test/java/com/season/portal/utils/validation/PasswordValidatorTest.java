package com.season.portal.utils.validation;

import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import javax.validation.ClockProvider;
import javax.validation.ConstraintValidatorContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class PasswordValidatorTest {

    private PasswordValidator pv;
    private ConstraintValidatorContext context;
    private ConstraintValidatorContext.ConstraintViolationBuilder builder;
    private boolean result = false;

    @BeforeEach
    public void setup() {
        pv =  new PasswordValidator();

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
    @DisplayName("Should validate passwords")
    @CsvSource({
            "*]?., false, utils_form_password_numChar",
            "11111, false, utils_form_password_numChar",
            "qq*q, false, utils_form_password_numChar",
            "aaaa1111, false, utils_form_password_atLeast",

            "11111, false, utils_form_password_numChar",
            "999999999, false, utils_form_password_atLeast",
            "999999999999999999999999999999999999999999999, false, utils_form_password_numChar",

            "Qwe123, false, utils_form_password_numChar",
            "Qwerty123, true, ",
            "sdfsaerbg%2!, true, ",
            "?.@wqwqwqeasdA, true, ",
            "??????????????12A, true, ",
    })
    void validateIsValidPassword(String password, String expected, String violation) {
        result = pv.isValid(password, context);
        assertThat(result).isEqualTo(Boolean.valueOf(expected));
        if(violation != null){
            Mockito.verify(context).buildConstraintViolationWithTemplate(violation);
        }
    }

    @ParameterizedTest
    @DisplayName("Should validate passwords with diferent types of characters")
    @CsvSource({
            "*]?., false, utils_form_password_atLeast",
            "11111, false, utils_form_password_atLeast",
            "q1A, true, ",
            "q1A*, true, ",
            "qq*q, false, utils_form_password_atLeast",
            "aaaa1111, false, utils_form_password_atLeast",
            "q1A*, true, ",
            "Qwerty123, true, ",
            "sdfsaerbg%2!, true, ",
            "?.@wqwqwqeasdA, true, ",
            "??????????????12A, true, ",
    })
    void validateAtLeastCharsPassword(String password, String expected, String violation) {
        result = pv.atLeastChars(password, context);
        assertThat(result).isEqualTo(Boolean.valueOf(expected));
        if(violation != null){
            Mockito.verify(context).buildConstraintViolationWithTemplate(violation);
        }
    }

    @ParameterizedTest
    @DisplayName("Should validate mutiple length passowrds")
    @CsvSource({
            "11111, false, utils_form_password_numChar",
            "999999999, true, ",
            "999999999999999999999999999999999999999999999, false, utils_form_password_numChar"
    })
    void validateLengthPassword(String password, String expected, String violation) {
        result = pv.numChar(password, context);
        assertThat(result).isEqualTo(Boolean.valueOf(expected));
        if(violation != null){
            Mockito.verify(context).buildConstraintViolationWithTemplate(violation);
        }

    }

    @Test
    @DisplayName("Should return false for small password")
    void numChar_invalidSmall() {
        result = pv.numChar("1111", context);
        assertFalse(result);
        Mockito.verify(context).buildConstraintViolationWithTemplate("utils_form_password_numChar");
    }

    @Test
    @DisplayName("Should return true for right number of characteres")
    void numChar_validNumberChar() {
        result = pv.numChar("999999999", context);
        assertTrue(result);
    }

    @Test
    @DisplayName("Should return false for password to big")
    void numChar_invalidBig() {
        result = pv.numChar("999999999999999999999999999sdfsdf", context);
        assertFalse(result);
        Mockito.verify(context).buildConstraintViolationWithTemplate("utils_form_password_numChar");
    }
}