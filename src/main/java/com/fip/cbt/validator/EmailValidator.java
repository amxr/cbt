package com.fip.cbt.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {
    
//    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@" +
//            "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    
    // RFC 5322 standard
    //private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    
    //Amir's original version
    private static final String EMAIL_PATTERN = "^(.+)@(\\S+)$";
    
    private static final Pattern PATTERN = Pattern.compile(EMAIL_PATTERN);
    
    @Override
    public boolean isValid(final String username, final ConstraintValidatorContext context) {
        return (validateEmail(username));
    }
    
    private boolean validateEmail(final String email){
        Matcher matcher = PATTERN.matcher(email);
        return matcher.matches();
    }
}
