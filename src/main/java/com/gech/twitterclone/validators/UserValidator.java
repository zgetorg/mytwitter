package com.gech.twitterclone.validators;

import com.gech.twitterclone.models.User;
import com.gech.twitterclone.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    @Autowired
    UserRepository userRepository;

    public boolean supports(Class<?> clazz){
        return User.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors){
        User user = (User) target;

        String email = user.getEmail();
        String password = user.getPassword();

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "user.firstName.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "user.lastName.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "user.email.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "user.password.empty");

        if(password.length() < 5){
            errors.rejectValue("password","user.password.tooShort");
        }
        if(userRepository.countByEmail(email)>0){
            errors.rejectValue("email", "user.email.duplicate");
        }
    }
}
