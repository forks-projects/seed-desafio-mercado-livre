package br.com.deveficiente.mercadolivre.produtos.perguntas;

import br.com.deveficiente.mercadolivre.produtos.Produto;
import br.com.deveficiente.mercadolivre.usuarios.Usuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.util.Assert;

public record NovaPerguntaRequest(
        @NotBlank
        String titulo
) {
        // 1 ICP: Produto
        // 1 ICP: Usuario
        // dica/anotações para quem usar o construtor saber os valores obrigatórios
        public PerguntaProduto toModel(@NotNull Produto produto, @NotNull Usuario usuario) {
                //self testing/ design by contrato
                Assert.notNull(produto, "Produto não pode ser nulo");
                Assert.notNull(usuario, "Usuario não pode ser nulo");
                // 1 ICP: PerguntaProduto
                return new PerguntaProduto(titulo(), produto, usuario);
        }
}
