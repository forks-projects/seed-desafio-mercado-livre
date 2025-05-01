package br.com.deveficiente.mercadolivre.compartilhado.validacao;

import br.com.deveficiente.mercadolivre.produtos.CaracteristicaRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CaracteristicasComNomesUnicosValidator implements ConstraintValidator<CaracteristicasComNomesUnicos, List<CaracteristicaRequest>> {

    // 1 ICP CaracteristicaRequest
    @Override
    public boolean isValid(List<CaracteristicaRequest> value, ConstraintValidatorContext context) {
        // 2 ICP if/else
        if (value == null || value.isEmpty()) return true;

        Set<String> nomesUnicos = new HashSet<>();
        // 1 ICP for
        for (CaracteristicaRequest caracteristica : value) {
            // 2 ICP if/else
            if (!nomesUnicos.add(caracteristica.nome())) {
                context.disableDefaultConstraintViolation();
                String mensagemErro = "As características devem ter nomes únicos. Característica duplicada: " + caracteristica.nome();
                context.buildConstraintViolationWithTemplate(mensagemErro).addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}

