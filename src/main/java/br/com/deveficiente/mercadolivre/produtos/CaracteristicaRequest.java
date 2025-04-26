package br.com.deveficiente.mercadolivre.produtos;

import jakarta.validation.constraints.NotBlank;

public record CaracteristicaRequest(
    @NotBlank
    String nome,
    @NotBlank
    String descricao
) {
    public Caracteristica toModel() {
        return new Caracteristica(nome(), descricao());
    }
}
