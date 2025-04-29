package br.com.deveficiente.mercadolivre.produtos;

import br.com.deveficiente.mercadolivre.categorias.Categoria;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProdutoTest {

    @Test
    @DisplayName("Deve criar produto com sucesso com características válidas")
    void deveCriarProdutoComSucesso() {
        Categoria categoria = new Categoria("Tecnologia");
        Caracteristica caracteristica1 = new Caracteristica("Tamanho", "6 polegadas");
        Caracteristica caracteristica2 = new Caracteristica("Cor", "Preto");
        Caracteristica caracteristica3 = new Caracteristica("Peso", "200g");

        Produto produto = new Produto(
                "Smartphone",
                BigDecimal.valueOf(1500),
                10,
                "Um ótimo smartphone.",
                categoria,
                Set.of(caracteristica1, caracteristica2, caracteristica3)
        );

        assertNotNull(produto);
        assertEquals("Smartphone", produto.nome);
        assertEquals(BigDecimal.valueOf(1500), produto.valor);
        assertEquals(10, produto.quantidade);
        assertEquals("Um ótimo smartphone.", produto.descricao);
        assertEquals(3, produto.caracteristicas.size());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar produto com menos de 3 características")
    void deveLancarExcecaoQuandoCaracteristicasForemMenosQueTres() {
        Categoria categoria = new Categoria("Tecnologia");
        Caracteristica caracteristica1 = new Caracteristica("Cor", "Branco");
        Caracteristica caracteristica2 = new Caracteristica("Peso", "150g");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Produto(
                        "Notebook",
                        BigDecimal.valueOf(3500),
                        5,
                        "Notebook potente.",
                        categoria,
                        Set.of(caracteristica1, caracteristica2)
                )
        );

        assertTrue(exception.getMessage().contains("deve ter pelo menos 3 características"));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar produto com características nulas")
    void deveLancarExcecaoQuandoCaracteristicasForNulo() {
        Categoria categoria = new Categoria("Tecnologia");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Produto(
                        "Tablet",
                        BigDecimal.valueOf(1000),
                        15,
                        "Tablet para estudos.",
                        categoria,
                        null
                )
        );

        assertTrue(exception.getMessage().contains("caracteristicas não pode ser nulo"));
    }
}
