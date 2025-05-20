package br.com.deveficiente.mercadolivre.produtos.caracteristicas;

import jakarta.validation.constraints.NotNull;

public record CaracteristicaResponse(
        Long id,
        String nome,
        String descricao
) {
    // dica/anotações para quem usar o construtor saber os valores obrigatórios
    public CaracteristicaResponse(@NotNull Caracteristica caracteristica) {
        this(caracteristica.getId(), caracteristica.getNome(), caracteristica.getDescricao());
    }
}
