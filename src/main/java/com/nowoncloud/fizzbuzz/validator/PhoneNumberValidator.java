package com.nowoncloud.fizzbuzz.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
	 
    @Override
    public void initialize(PhoneNumber param) {
    }
 
    @Override
    public boolean isValid(String phoneNo, ConstraintValidatorContext ctx) {
        if(phoneNo == null){
            return false;
        }
        //validate phone numbers of format "1234567890". Nothing other than 10 numbers is allowed in the phone number input field.
        if (phoneNo.matches("\\d{10}")) {
        	return true;
        } else { 
        	return false;
        }
    }
 
}