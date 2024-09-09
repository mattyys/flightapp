package org.tokioschool.flightapp.core.validator;

import java.lang.annotation.*;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;


@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = EnumValidImpl.class)
public @interface EnumValid {

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String message() default "invalid value";


    @NotNull
    Class<? extends Enum<?>> target();

    boolean required() default true;

}
