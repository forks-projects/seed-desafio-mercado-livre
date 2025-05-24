package br.com.deveficiente.mercadolivre.compras;

import br.com.deveficiente.mercadolivre.compartilhado.validacao.ExisteId;
import br.com.deveficiente.mercadolivre.produtos.Produto;
import br.com.deveficiente.mercadolivre.usuarios.Usuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.util.Assert;

public record NovaCompraRequest(
        @Positive
        Integer quantidade,

        @NotNull
        @ExisteId(classeDaEntidade = Produto.class, nomeDoCampo = "id")
        Long idProduto,

        @NotBlank
        String formaPagamento
) {
    // dica/anotações para quem usar o construtor saber os valores obrigatórios
    public Compra toModel(@NotNull Usuario usuario, @NotNull Produto produto) {
        //self testing/ design by contrato
        // 1 ICP assert
        Assert.notNull(usuario, "Usuario não pode ser nulo");
        // 1 ICP assert
        Assert.notNull(produto, "Produto não pode ser nulo");
        return new Compra(usuario, produto, quantidade, formaPagamento);
    }
}
