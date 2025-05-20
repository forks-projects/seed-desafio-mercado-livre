package br.com.deveficiente.mercadolivre.produtos.perguntas;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record PerguntaProdutoResponse(
    Long id,
    String titulo,
    String emailCliente,
    LocalDateTime dataHoraRegistro
) {
    // dica/anotações para quem usar o construtor saber os valores obrigatórios
    public PerguntaProdutoResponse(@NotNull PerguntaProduto pergunta) {
        this(pergunta.getId(),
                pergunta.getTitulo(),
                pergunta.getEmailCliente(),
                pergunta.getDataHoraRegistro());
    }
}
