package br.com.deveficiente.mercadolivre.compartilhado;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CaracteristicasComNomesUnicosValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CaracteristicasComNomesUnicos {
    String message() default "As características devem ter nomes únicos";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
