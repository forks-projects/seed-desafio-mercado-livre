package br.com.deveficiente.mercadolivre.compartilhado;

import br.com.deveficiente.mercadolivre.produtos.CaracteristicaRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CaracteristicasComNomesUnicosValidator implements ConstraintValidator<CaracteristicasComNomesUnicos, List<CaracteristicaRequest>> {

    @Override
    public boolean isValid(List<CaracteristicaRequest> value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) return true;

        Set<String> nomesUnicos = new HashSet<>();
        for (CaracteristicaRequest caracteristica : value) {
            if (!nomesUnicos.add(caracteristica.nome())) {
                return false;
            }
        }
        return true;
    }
}

