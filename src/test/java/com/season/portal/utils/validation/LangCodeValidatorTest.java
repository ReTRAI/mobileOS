package com.season.portal.utils.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import javax.validation.ConstraintValidatorContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LangCodeValidatorTest {

    private LangCodeValidator validator;
    private ConstraintValidatorContext context;
    private ConstraintValidatorContext.ConstraintViolationBuilder builder;
    private boolean result = false;

    @BeforeEach
    public void setup() {
        validator =  new LangCodeValidator();

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
    @DisplayName("Should validate language codes")
    @CsvSource({

            "null, false, utils_form_langCode_invalid",
            "1, false, utils_form_langCode_invalid",
            "qq*q, false, utils_form_langCode_invalid",
            "aaaa1111, false, utils_form_langCode_invalid",

            "11111, false, utils_form_langCode_invalid",
            "999999999, false, utils_form_langCode_invalid",
            "p, false, utils_form_langCode_invalid",

            "sp, false, utils_form_langCode_invalid",

            "pt, true, ",
            "en, true, ",
    })
    void validateIsValidPassword(String password, String expected, String violation) {
        result = validator.isValid(password, context);
        assertThat(result).isEqualTo(Boolean.valueOf(expected));
        if(violation != null){
            Mockito.verify(context).buildConstraintViolationWithTemplate(violation);
        }
    }

}