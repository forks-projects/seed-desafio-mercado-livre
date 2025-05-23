package br.com.deveficiente.mercadolivre.produtos;

import br.com.deveficiente.mercadolivre.categorias.Categoria;
import br.com.deveficiente.mercadolivre.compartilhado.databuilders.CaracteristicaRequestBuilder;
import br.com.deveficiente.mercadolivre.produtos.caracteristicas.CaracteristicaRequest;
import br.com.deveficiente.mercadolivre.usuarios.SenhaLimpa;
import br.com.deveficiente.mercadolivre.usuarios.Usuario;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NovoProdutoRequestTest {

    @Mock
    private EntityManager entityManager;

    List<CaracteristicaRequest> caracteristicas = new ArrayList<>();

    @BeforeEach
    public void setup() {
        caracteristicas = List.of(
                CaracteristicaRequestBuilder.umaCaracteristica().comNome("cor").comDescricao("preto").build(),
                CaracteristicaRequestBuilder.umaCaracteristica().comNome("peso").comDescricao("200g").build(),
                CaracteristicaRequestBuilder.umaCaracteristica().comNome("material").comDescricao("metal").build()
        );
    }

    @Test
    @DisplayName("Deve criar um produto válido a partir de NovoProdutoRequest")
    void deveCriarProdutoValido() {
        Categoria categoria = new Categoria("Tecnologia");
        when(entityManager.find(Categoria.class, 1)).thenReturn(categoria);

        Usuario usuario = new Usuario("adriano@email.com", new SenhaLimpa("123456"));
        NovoProdutoRequest request = new NovoProdutoRequest(
                "Smartphone",
                BigDecimal.valueOf(1500),
                10,
                "Um smartphone top de linha",
                1,
                caracteristicas
        );

        Produto produto = request.toModel(entityManager, usuario);

        assertNotNull(produto);
        assertEquals(request.nome(), produto.getNome());
        assertEquals(request.valor(), produto.getValor());
        assertEquals(request.descricao(), produto.getDescricao());
        assertEquals(3, produto.getCaracteristicas().size());
    }

    @Test
    @DisplayName("Deve lançar exceção se categoria não for encontrada")
    void deveLancarExcecaoSeCategoriaNaoEncontrada() {
        when(entityManager.find(Categoria.class, 999)).thenReturn(null);

        NovoProdutoRequest request = new NovoProdutoRequest(
                "Smartphone",
                BigDecimal.valueOf(1500),
                10,
                "Um smartphone top de linha",
                999,
                caracteristicas
        );
        Usuario usuario = new Usuario("adriano@email.com", new SenhaLimpa("123456"));
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> request.toModel(entityManager, usuario)
        );

        assertTrue(exception.getMessage().contains("Categoria deve existir"));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar produto sem características")
    void deveLancarExcecaoSeCaracteristicasForemNulas() {
        Categoria categoria = new Categoria("Tecnologia");
        when(entityManager.find(Categoria.class, 1)).thenReturn(categoria);

        NovoProdutoRequest request = new NovoProdutoRequest(
                "Smartphone",
                BigDecimal.valueOf(1500),
                10,
                "Um smartphone top de linha",
                1,
                null
        );
        Usuario usuario = new Usuario("adriano@email.com", new SenhaLimpa("123456"));

        assertThrows(NullPointerException.class,
                () -> request.toModel(entityManager, usuario)
        );
    }
}
