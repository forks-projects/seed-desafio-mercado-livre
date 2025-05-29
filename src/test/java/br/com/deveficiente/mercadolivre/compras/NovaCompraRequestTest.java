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
        NovaCompraRequest request = new NovaCompraRequest(2, 1L, "PAYPAL");

        Compra compra = request.toModel(usuario, produto);

        assertNotNull(compra);
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
