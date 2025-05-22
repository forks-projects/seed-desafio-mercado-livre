package br.com.deveficiente.mercadolivre.produtos.perguntas;


import br.com.deveficiente.mercadolivre.categorias.Categoria;
import br.com.deveficiente.mercadolivre.produtos.Produto;
import br.com.deveficiente.mercadolivre.produtos.caracteristicas.Caracteristica;
import br.com.deveficiente.mercadolivre.usuarios.SenhaLimpa;
import br.com.deveficiente.mercadolivre.usuarios.Usuario;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NovaPerguntaRequestTest {
    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Deve cirar PerguntaProduto com dados validos")
    void deveCirarPerguntaProdutoComDadosValidos() {
        Categoria categoria = new Categoria("Tecnologia");
        Usuario usuarioVendedor = new Usuario("adriano@email.com", new SenhaLimpa("123456"));
        Usuario usuarioCliente = new Usuario("adriano@email.com", new SenhaLimpa("123456"));
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
                usuarioVendedor,
                caracteristicas
        );
        NovaPerguntaRequest request = new NovaPerguntaRequest("titulo");
        PerguntaProduto perguntaProduto = request.toModel(produto, usuarioCliente);

        Assertions.assertNotNull(perguntaProduto);
        Assertions.assertEquals(request.titulo(), perguntaProduto.getTitulo());
        Assertions.assertEquals(produto.getEmailVendedor(), perguntaProduto.getEmailVendedor());
        Assertions.assertEquals(usuarioCliente.getLogin(), perguntaProduto.getEmailCliente());
    }

    @DisplayName("Deve falahar com titulo inválido")
    @ParameterizedTest(name = "[{index}] titulo = {0}")
    @NullAndEmptySource
    void deveFalaharComTituloInválido(String titulo) {
        NovaPerguntaRequest request = new NovaPerguntaRequest(titulo);
        Set<ConstraintViolation<NovaPerguntaRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        String mensagemErro = violations.iterator().next().getMessage();
        assertEquals(mensagemErro, "não deve estar em branco");
    }

    @Test
    @DisplayName("Deve lançar excecao ao converter para modelo com produto nulo")
    void deveLançarExcecaoAoConverterParaModeloComProdutoNulo() {
        NovaPerguntaRequest request = new NovaPerguntaRequest("titulo");
        Produto produto = null;
        Usuario usuario = new Usuario();
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> request.toModel(produto, usuario));
        String mensagemErro = illegalArgumentException.getMessage();
        assertEquals("Produto não pode ser nulo", mensagemErro);
    }

    @Test
    @DisplayName("Deve lançar excecao ao converter para modelo com usuario nulo")
    void deveLançarExcecaoAoConverterParaModeloComUsuarioNulo() {
        NovaPerguntaRequest request = new NovaPerguntaRequest("titulo");
        Produto produto = new Produto();
        Usuario usuario = null;
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> request.toModel(produto, usuario));
        String mensagemErro = illegalArgumentException.getMessage();
        assertEquals("Usuario não pode ser nulo", mensagemErro);
    }

}