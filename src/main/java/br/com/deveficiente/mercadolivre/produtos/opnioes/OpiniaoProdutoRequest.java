package br.com.deveficiente.mercadolivre.produtos.opnioes;

import br.com.deveficiente.mercadolivre.produtos.Produto;
import br.com.deveficiente.mercadolivre.usuarios.Usuario;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.util.Assert;

public record OpiniaoProdutoRequest(
        @Min(1)
        @Max(5)
        @NotNull
        Integer nota,
        @NotBlank
        String titulo,
        @NotBlank
        @Length(min = 3, max = 500)
        String descricao
) {
    // dica/anotações para quem usar o construtor saber os valores obrigatórios
    public OpiniaoProduto toModel(@NotNull Produto produto, @NotNull Usuario usuario) {
        //self testing/ design by contrato
        Assert.notNull(produto, "Produto não pode ser nulo");
        Assert.notNull(usuario, "Usuario não pode ser nulo");
        return new OpiniaoProduto(nota(), titulo(), descricao(), produto, usuario);
    }
}
