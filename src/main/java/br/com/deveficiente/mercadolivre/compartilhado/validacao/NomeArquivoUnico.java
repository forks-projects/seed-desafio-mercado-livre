package br.com.deveficiente.mercadolivre.compartilhado.validacao;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = NomeArquivoUnicoValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface NomeArquivoUnico {
    String message() default "Não pode haver arquivos com nomes duplicados";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
