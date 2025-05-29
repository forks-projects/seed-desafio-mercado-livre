package br.com.deveficiente.mercadolivre.produtos;

import br.com.deveficiente.mercadolivre.categorias.Categoria;
import br.com.deveficiente.mercadolivre.produtos.caracteristicas.Caracteristica;
import br.com.deveficiente.mercadolivre.produtos.imagens.ImagemProduto;
import br.com.deveficiente.mercadolivre.produtos.perguntas.PerguntaProduto;
import br.com.deveficiente.mercadolivre.usuarios.SenhaLimpa;
import br.com.deveficiente.mercadolivre.usuarios.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProdutoTest {

    @DisplayName("Deve criar produto com sucesso com características válidas")
    @ParameterizedTest(name = "[index] {1}")
    @MethodSource("listarCaracteristicasValidas")
    void deveCriarProdutoComSucesso(Set<Caracteristica> caracteristicas, String descricaoTeste) {
        Categoria categoria = new Categoria("Tecnologia");
        Usuario usuario = new Usuario("adriano@email.com", new SenhaLimpa("123456"));

        Produto produto = new Produto(
                "Smartphone",
                BigDecimal.valueOf(1500),
                10,
                "Um ótimo smartphone.",
                categoria,
                usuario,
                caracteristicas
        );

        assertNotNull(produto);
        assertEquals("Smartphone", produto.getNome());
        assertEquals(BigDecimal.valueOf(1500), produto.getValor());
        assertEquals("Um ótimo smartphone.", produto.getDescricao());
        assertEquals(caracteristicas.size(), produto.getCaracteristicas().size());
    }

    @DisplayName("Deve lançar exceção ao tentar criar produto com menos de 3 características")
    @ParameterizedTest(name = "[{index}] {1}")
    @MethodSource("listarCaracteristicasInvalidas")
    void deveLancarExcecaoQuandoCaracteristicasForemMenosQueTres(Set<Caracteristica> caracteristicas, String descricaoTeste) {
        Categoria categoria = new Categoria("Tecnologia");
        Usuario usuario = new Usuario("adriano@email.com", new SenhaLimpa("123456"));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Produto(
                        "Notebook",
                        BigDecimal.valueOf(3500),
                        5,
                        "Notebook potente.",
                        categoria,
                        usuario,
                        caracteristicas
                )
        );

        assertTrue(exception.getMessage().contains("deve ter pelo menos 3 características"));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar produto com características nulas")
    void deveLancarExcecaoQuandoCaracteristicasForNulo() {
        Categoria categoria = new Categoria("Tecnologia");
        Usuario usuario = new Usuario("adriano@email.com", new SenhaLimpa("123456"));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Produto(
                        "Tablet",
                        BigDecimal.valueOf(1000),
                        15,
                        "Tablet para estudos.",
                        categoria,
                        usuario,
                        null
                )
        );

        assertTrue(exception.getMessage().contains("caracteristicas não pode ser nulo"));
    }

    @Test
    @DisplayName("Deve adicionar pergunta no Produto")
    void deveAdicionarPerguntaNoProduto() {
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

        assertDoesNotThrow(() -> produto.adicionarPergunta(pergunta));
    }

    @Test
    @DisplayName("Deve lancarExceptionQuandoPerguntaNula")
    void deveLancarExceptionQuandoPerguntaNula() {
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
        PerguntaProduto pergunta = null;

        assertThrows(IllegalArgumentException.class, () -> produto.adicionarPergunta(pergunta));
    }

    @Test
    @DisplayName("Deve lancarException quando adicionar lista de imagem vazia")
    void deveLancarExceptionQuandoAdicionarListaImagemVazia() {
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
        Set<ImagemProduto> listaImagemVazia = Set.of();

        assertThrows(IllegalArgumentException.class, () -> produto.adicionarImagens(listaImagemVazia));
    }

    @DisplayName("Deve abater quantidade de produto")
    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 10 })
    void deveAbaterQuantidadeDeProduto(Integer quantidadeAbate) {
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

        produto.abaterEstoque(quantidadeAbate);

        int quantidadeEsperada = 10 - quantidadeAbate;
        assertEquals(quantidadeEsperada, produto.getQuantidade());
    }

    @DisplayName("Deve lancar excecao quando quantidade invalida")
    @ParameterizedTest
    @ValueSource(ints = {-10, -1, 0})
    void deveLancarExcecaoQuandoQuantidadeInvalida(Integer quantidadeAbate) {
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

        assertThrows(IllegalArgumentException.class, () -> produto.abaterEstoque(quantidadeAbate));
    }

    @DisplayName("Deve lancar excecao ao abater quantidade maior que estoque")
    @ParameterizedTest
    @ValueSource(ints = {11, 12, 999 })
    void deveLancarExcecaoAoAbaterQuantidadeMaiorQueEstoque(Integer quantidadeAbate) {
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

        assertThrows(IllegalArgumentException.class, () -> produto.abaterEstoque(quantidadeAbate));
    }

    public static Stream<Arguments> listarCaracteristicasInvalidas() {
        Caracteristica caracteristica1 = new Caracteristica("Tamanho", "6 polegadas");
        Caracteristica caracteristica2 = new Caracteristica("Peso", "200g");
        return Stream.of(
                Arguments.of(Set.of(), "lista de caracteristicas vazia"),
                Arguments.of(Set.of(caracteristica1), "lista com 1 caracteristica"),
                Arguments.of(Set.of(caracteristica1, caracteristica2), "lista com 2 caracteristicas")
        );
    }

    public static Stream<Arguments> listarCaracteristicasValidas() {
        Caracteristica caracteristica1 = new Caracteristica("Tamanho", "6 polegadas");
        Caracteristica caracteristica2 = new Caracteristica("Cor", "Preto");
        Caracteristica caracteristica3 = new Caracteristica("Peso", "200g");
        Caracteristica caracteristica4 = new Caracteristica("Sistema Operacional", "Android");
        return Stream.of(
                Arguments.of(
                        Set.of(caracteristica1, caracteristica2, caracteristica3),
                        "lista com 3 caracteristicas validas"
                ),
                Arguments.of(
                        Set.of(caracteristica1, caracteristica2, caracteristica3, caracteristica4),
                        "lista com 4 caracteristicas validas"
                )
        );
    }
}
