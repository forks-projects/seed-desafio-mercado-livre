package br.com.deveficiente.mercadolivre.produtos.perguntas;

import br.com.deveficiente.mercadolivre.categorias.Categoria;
import br.com.deveficiente.mercadolivre.produtos.Produto;
import br.com.deveficiente.mercadolivre.produtos.caracteristicas.Caracteristica;
import br.com.deveficiente.mercadolivre.usuarios.SenhaLimpa;
import br.com.deveficiente.mercadolivre.usuarios.Usuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

class PerguntaProdutoResponseTest {
    @Test
    @DisplayName("Deve criar pergunta response")
    void deveCriarPerguntaResponse() {
        Categoria categoria = new Categoria("Tecnologia");
        Usuario usuario = new Usuario("adriano@email.com", new SenhaLimpa("123456"));
        Set<Caracteristica> caracteristicas = Set.of(
                new Caracteristica("Tamanho", "6 polegadas"),
                new Caracteristica("Cor", "Preto"),
                new Caracteristica("Peso", "200g")
        );
        Produto produto = new Produto(
                "Smartphone",
                BigDecimal.valueOf(1500),
                10,
                "Um ótimo smartphone.",
                categoria,
                usuario,
                caracteristicas
        );
        PerguntaProduto pergunta = new PerguntaProduto("titulo", produto, usuario);

        PerguntaProdutoResponse perguntaProdutoResponse = new PerguntaProdutoResponse(pergunta);

        Assertions.assertEquals(perguntaProdutoResponse.titulo(), pergunta.getTitulo());
        Assertions.assertEquals(perguntaProdutoResponse.emailCliente(), usuario.getLogin());
        Assertions.assertEquals(perguntaProdutoResponse.dataHoraRegistro(), pergunta.getDataHoraRegistro());
    }
}