package br.com.deveficiente.mercadolivre.produtos.opnioes;


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

class OpiniaoProdutoResponseTest {
    @Test
    @DisplayName("Deve criar opiniao de produto response")
    void deveCriarOpiniaoDeProdutoResponse() {
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
        String tituloOpiniao = "muito bom";
        String descricaoOpiniao = "Produto de alta qualidade";
        int nota = 5;
        OpiniaoProduto opiniaoProduto = new OpiniaoProduto(nota, tituloOpiniao, descricaoOpiniao, produto, usuario);

        OpiniaoProdutoResponse opiniaoProdutoResponse = new OpiniaoProdutoResponse(opiniaoProduto);

        Assertions.assertEquals(opiniaoProdutoResponse.nota(), nota);
        Assertions.assertEquals(opiniaoProdutoResponse.titulo(), tituloOpiniao);
        Assertions.assertEquals(opiniaoProdutoResponse.descricao(), descricaoOpiniao);
        Assertions.assertEquals(opiniaoProdutoResponse.emailCliente(), usuario.getLogin());
    }
}