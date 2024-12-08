package com.springa.springa.Products;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidationMultipart implements ConstraintValidator<ValidationCustomMultipart, MultipartFile>{
    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context){
        return !value.isEmpty();
    }
}
