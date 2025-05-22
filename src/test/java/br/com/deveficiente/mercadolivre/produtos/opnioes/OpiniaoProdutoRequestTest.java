package br.com.deveficiente.mercadolivre.produtos.opnioes;

import br.com.deveficiente.mercadolivre.produtos.Produto;
import br.com.deveficiente.mercadolivre.usuarios.SenhaLimpa;
import br.com.deveficiente.mercadolivre.usuarios.Usuario;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OpiniaoProdutoRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Deve criar OpiniaoProduto com dados válidos")
    void deveCriarOpiniaoComDadosValidos() {
        OpiniaoProdutoRequest request = new OpiniaoProdutoRequest(5, "Excelente", "Produto de alta qualidade");

        Produto produto = new Produto();
        Usuario usuario = new Usuario("adriano@email.com", new SenhaLimpa("123456"));

        OpiniaoProduto opiniao = request.toModel(produto, usuario);
        assertNotNull(opiniao);
        assertEquals(request.titulo(), opiniao.getTitulo());
        assertEquals(request.descricao(), opiniao.getDescricao());
        assertEquals(request.nota(), opiniao.getNota());
    }

    @DisplayName("Deve falhar com nota fora do intervalo = {0}")
    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 6,7,99})
    void deveFalharComNotaInvalida(Integer nota) {
        OpiniaoProdutoRequest request = new OpiniaoProdutoRequest(nota, "Bom", "Muito bom mesmo");

        Set<ConstraintViolation<OpiniaoProdutoRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        String mensagemErro = violations.iterator().next().getMessage();
        assertTrue(mensagemErro.contains("deve ser menor que ou igual à 5") || mensagemErro.contains("deve ser maior que ou igual à 1"));
    }

    @DisplayName("Deve falhar com título em branco")
    @ParameterizedTest
    @NullAndEmptySource
    void deveFalharComTituloEmBranco(String titulo) {
        OpiniaoProdutoRequest request = new OpiniaoProdutoRequest(4, titulo, "Descrição válida");

        Set<ConstraintViolation<OpiniaoProdutoRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        String mensagemErro = violations.iterator().next().getMessage();
        assertTrue(mensagemErro.contains("não deve estar em branco"));
    }

    @DisplayName("Deve falhar com descricao em branco")
    @ParameterizedTest
    @NullAndEmptySource
    void deveFalharComDescricaoEmBranco(String descricao) {
        OpiniaoProdutoRequest request = new OpiniaoProdutoRequest(3, "Ok", descricao);

        Set<ConstraintViolation<OpiniaoProdutoRequest>> violations = validator.validate(request);

        if (Objects.isNull(descricao)) {
            assertEquals(1, violations.size());
        } else {
            assertEquals(2, violations.size());
        }
        String mensagemErro = violations.iterator().next().getMessage();
        assertTrue(mensagemErro.contains("não deve estar em branco") || mensagemErro.contains("o comprimento deve ser entre 3 e 500"));
    }

    @DisplayName("Deve falhar com descrição com tamanho invalido")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 501, 502, 999})
    void deveFalharComDescricaoCurta(int numeroCaracteres) {
        String descricao = String.valueOf("x").repeat(numeroCaracteres);
        OpiniaoProdutoRequest request = new OpiniaoProdutoRequest(3, "Ok", descricao);

        Set<ConstraintViolation<OpiniaoProdutoRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        String mensagemErro = violations.iterator().next().getMessage();
        assertTrue(mensagemErro.contains("o comprimento deve ser entre 3 e 500"));
    }

    @Test
    @DisplayName("Deve lançar exceção ao converter para modelo com produto nulo")
    void deveLancarExcecaoComProdutoNulo() {
        OpiniaoProdutoRequest request = new OpiniaoProdutoRequest(3, "Bom", "Produto confiável");

        Usuario usuario = new Usuario();

        assertThrows(IllegalArgumentException.class, () -> request.toModel(null, usuario));
    }

    @Test
    @DisplayName("Deve lançar exceção ao converter para modelo com usuário nulo")
    void deveLancarExcecaoComUsuarioNulo() {
        OpiniaoProdutoRequest request = new OpiniaoProdutoRequest(3, "Bom", "Produto confiável");

        Produto produto = new Produto();

        assertThrows(IllegalArgumentException.class, () -> request.toModel(produto, null));
    }
}
