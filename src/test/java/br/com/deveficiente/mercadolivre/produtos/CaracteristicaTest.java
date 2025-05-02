package br.com.deveficiente.mercadolivre.produtos;

import br.com.deveficiente.mercadolivre.categorias.Categoria;
import br.com.deveficiente.mercadolivre.usuarios.SenhaLimpa;
import br.com.deveficiente.mercadolivre.usuarios.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CaracteristicaTest {
    @Test
    @DisplayName("Deve vincular caracteristica a um produto real")
    void deveVincularCaracteristicaAoProdutoReal() {
        Set<Caracteristica> caracteristicas = Set.of(
                new Caracteristica("Cor", "Preto"),
                new Caracteristica("Peso", "200g"),
                new Caracteristica("Material", "Plástico")
        );
        Usuario usuario = new Usuario("adriano@email.com", new SenhaLimpa("123456"));
        Produto produto = new Produto(
                "Produto Teste",
                BigDecimal.valueOf(100),
                10,
                "Descrição de teste",
                new Categoria("Categoria Teste"),
                usuario,
                caracteristicas
        );

        Caracteristica caracteristica = new Caracteristica("Tamanho", "6.5 polegadas");

        assertDoesNotThrow(() -> caracteristica.vinculaAProduto(produto));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar vincular caracteristica com produto nulo")
    void deveLancarExcecaoQuandoProdutoForNulo() {
        Caracteristica caracteristica = new Caracteristica("Peso", "200g");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> caracteristica.vinculaAProduto(null)
        );

        assertTrue(exception.getMessage().contains("produto não pode ser nulo"));
    }
}
