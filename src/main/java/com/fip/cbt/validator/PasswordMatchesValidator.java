package com.fip.cbt.validator;


import com.fip.cbt.controller.request.SignUpRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
    @Override
    public void initialize(final PasswordMatches constraintAnnotation) {
        //ConstraintValidator.super.initialize(constraintAnnotation);
    }
    
    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        final SignUpRequest user = (SignUpRequest) value;
        return user.getPassword().equals(user.getMatchingPassword());
    }
}
