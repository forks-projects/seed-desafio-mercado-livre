package br.com.deveficiente.mercadolivre.produtos;


import br.com.deveficiente.mercadolivre.categorias.Categoria;
import br.com.deveficiente.mercadolivre.produtos.caracteristicas.Caracteristica;
import br.com.deveficiente.mercadolivre.produtos.imagens.ImagemProduto;
import br.com.deveficiente.mercadolivre.produtos.perguntas.PerguntaProduto;
import br.com.deveficiente.mercadolivre.usuarios.SenhaLimpa;
import br.com.deveficiente.mercadolivre.usuarios.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DetalhesProdutoResponseTest {
    @Test
    @DisplayName("Deve criar produto response")
    void deveCriarProdutoResponse() {
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
        ImagemProduto imagem1 = new ImagemProduto("imagem1.jpg", "http://bucket/imagens/imagem1.jpg");
        ImagemProduto imagem2 = new ImagemProduto("imagem2.jpg", "http://bucket/imagens/imagem2.jpg");
        Set<ImagemProduto> imagens = Set.of(imagem1, imagem2);
        produto.adicionarImagens(imagens);
        produto.adicionarPergunta(pergunta);

        DetalhesProdutoResponse detalhesProdutoResponse = new DetalhesProdutoResponse(produto);

        assertEquals(detalhesProdutoResponse.nome(), produto.getNome());
        assertEquals(detalhesProdutoResponse.preco(), produto.getValor());
        assertEquals(detalhesProdutoResponse.notaMedia(), produto.getNotaMedia());
        assertEquals(detalhesProdutoResponse.numeroTotalNotas(), (Integer) 0);
        assertEquals(detalhesProdutoResponse.caractesristicas().size(), (Integer) 3);
        assertEquals(detalhesProdutoResponse.imagens().size(), (Integer) 2);
        assertEquals(detalhesProdutoResponse.opinioes().size(), (Integer) 0);
        assertEquals(detalhesProdutoResponse.perguntas().size(), (Integer) 1);
    }
}