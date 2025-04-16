package com.electronic.store.Validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageNameValidatorImpl implements ConstraintValidator<ImageNameValidator, String> {

    private Logger logger= LoggerFactory.getLogger("ImageNameValidatorImpl.class");
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {


        if(s.isBlank()){
            s=s+"add some image";
            logger.info("Message for validator: "+s);
            return false;
        }else {
            logger.info("Message for validator: "+s);
            return true;

        }
    }
}
