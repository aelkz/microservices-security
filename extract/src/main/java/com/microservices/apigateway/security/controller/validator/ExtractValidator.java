package com.microservices.apigateway.security.controller.validator;

import com.microservices.apigateway.security.model.ExtractRecord;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class ExtractValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ExtractRecord.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "status", "validation.message.field.required");
    }

}
