package br.com.deveficiente.mercadolivre.compras;

import br.com.deveficiente.mercadolivre.categorias.Categoria;
import br.com.deveficiente.mercadolivre.produtos.Produto;
import br.com.deveficiente.mercadolivre.produtos.caracteristicas.Caracteristica;
import br.com.deveficiente.mercadolivre.usuarios.SenhaLimpa;
import br.com.deveficiente.mercadolivre.usuarios.Usuario;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class NovaCompraRequestTest {

    @Mock
    private Validator validator;

    private Produto criaProdutoValido() {
        Categoria categoria = new Categoria("Eletrônicos");
        Usuario dono = new Usuario("vendedor@email.com", new SenhaLimpa("123456"));
        Set<Caracteristica> caracteristicas = Set.of(
                new Caracteristica("Tela", "6 polegadas"),
                new Caracteristica("Memória", "128GB"),
                new Caracteristica("Câmera", "12MP")
        );
        return new Produto("Celular",
                BigDecimal.valueOf(1200),
                50,
                "Smartphone novo",
                categoria,
                dono,
                caracteristicas);
    }

    private Usuario criaUsuario() {
        return new Usuario("cliente@email.com", new SenhaLimpa("123456"));
    }

    @Test
    @DisplayName("Deve criar Compra com dados válidos")
    void deveCriarCompraComDadosValidos() {
        Produto produto = criaProdutoValido();
        Usuario usuario = criaUsuario();
        int quantidade = 2;
        NovaCompraRequest request = new NovaCompraRequest(quantidade, 1L, "PAYPAL");

        Compra compra = request.toModel(usuario, produto);

        long idCompra = 1L;
        ReflectionTestUtils.setField(compra, "id", idCompra);
        assertNotNull(compra);
        assertEquals(compra.getId(), idCompra);
        assertEquals(compra.getEmailVendedor(), produto.getEmailVendedor());
        assertEquals(compra.getIdVendedor(), produto.getIdVendedor());
        assertEquals(compra.getEmailCliente(), usuario.getLogin());
        assertEquals(compra.getIdCliente(), usuario.getId());
        assertEquals(compra.getProduto(), produto);
        assertEquals(compra.getQuantidade(), quantidade);
    }

    @Test
    @DisplayName("Deve lançar exceção se produto for nulo")
    void deveLancarExcecaoSeProdutoForNulo() {
        Usuario usuario = criaUsuario();
        NovaCompraRequest request = new NovaCompraRequest(1, 1L, "boleto");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> request.toModel(usuario, null));

        assertEquals("Produto não pode ser nulo", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção se usuário for nulo")
    void deveLancarExcecaoSeUsuarioForNulo() {
        Produto produto = criaProdutoValido();
        NovaCompraRequest request = new NovaCompraRequest(1, 1L, "boleto");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> request.toModel(null, produto));
        assertEquals("Usuario não pode ser nulo", ex.getMessage());
    }
}
