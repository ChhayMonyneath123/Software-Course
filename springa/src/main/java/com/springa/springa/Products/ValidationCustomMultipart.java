package com.springa.springa.Products;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidationMultipart.class)
public @interface ValidationCustomMultipart {
    String message() default "Please Upload imgae";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}