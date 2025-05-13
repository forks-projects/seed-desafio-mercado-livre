package br.com.deveficiente.mercadolivre.produtos.caracteristicas;

import io.jsonwebtoken.lang.Assert;
import jakarta.validation.constraints.NotBlank;

public record CaracteristicaRequest(
    @NotBlank
    String nome,
    @NotBlank
    String descricao
) {
    public Caracteristica toModel() {
        //self testing/ design by contrato
        Assert.hasLength(nome(), "nome não deve estar em branco");
        Assert.hasLength(descricao(), "descrição não deve estar em branco");
        return new Caracteristica(nome(), descricao());
    }
}
