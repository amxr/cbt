package com.fip.cbt.validator;

import com.google.common.base.Joiner;
import org.passay.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {
    
    @Override
    public void initialize(final ValidPassword constraintAnnotation) {
        //ConstraintValidator.super.initialize(constraintAnnotation);
    }
    
    @Override
    public boolean isValid(final String password, final ConstraintValidatorContext context) {
        
        final PasswordValidator passwordValidator = new PasswordValidator(Arrays.asList(
                new LengthRule(6, 16),
//                new CharacterRule(EnglishCharacterData.UpperCase, 1)
//                new CharacterRule(EnglishCharacterData.LowerCase,1),
//                new CharacterRule(EnglishCharacterData.Digit, 1),
//                new CharacterRule(EnglishCharacterData.Special, 1),
                new WhitespaceRule()
        ));
        final RuleResult result = passwordValidator.validate(new PasswordData(password));
        if(result.isValid()) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(Joiner.on(",").join(passwordValidator.getMessages(result)))
                .addConstraintViolation();
        return false;
    }
}
