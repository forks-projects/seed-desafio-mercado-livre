package br.com.deveficiente.mercadolivre.produtos.opnioes;

import jakarta.validation.constraints.NotNull;

public record OpiniaoProdutoResponse(
    Long id,
    Integer nota,
    String titulo,
    String descricao,
    String emailCliente
) {
    // dica/anotações para quem usar o construtor saber os valores obrigatórios
    public OpiniaoProdutoResponse(@NotNull OpiniaoProduto opiniao) {
        this(
                opiniao.getId(),
                opiniao.getNota(),
                opiniao.getTitulo(),
                opiniao.getDescricao(),
                opiniao.getEmailCliente()
        );
    }
}
