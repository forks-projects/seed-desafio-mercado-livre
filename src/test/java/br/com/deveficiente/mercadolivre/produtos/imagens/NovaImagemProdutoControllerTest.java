package br.com.deveficiente.mercadolivre.produtos.imagens;

import br.com.deveficiente.mercadolivre.compartilhado.seguranca.AutorizacaoHelper;
import br.com.deveficiente.mercadolivre.compartilhado.seguranca.TokenManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.jqwik.api.ForAll;
import net.jqwik.api.Label;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.AlphaChars;
import net.jqwik.api.constraints.BigRange;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.StringLength;
import net.jqwik.api.lifecycle.BeforeProperty;
import net.jqwik.spring.JqwikSpringSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@JqwikSpringSupport
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class NovaImagemProdutoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TokenManager tokenManager;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Set<String> produtosCadastrados = new HashSet<>();

    private List<Map<String, String>> listaCaracteristicas;

    @BeforeProperty
    void setup() {
        listaCaracteristicas = List.of(
                Map.of("nome", "Cor", "descricao", "Preto"),
                Map.of("nome", "Tamanho", "descricao", "Médio"),
                Map.of("nome", "Peso", "descricao", "1kg")
        );
    }

    @Property(tries = 10)
    @Label("Upload de imagens com sucesso")
    void cadastrarProdutoComSucesso(
            @ForAll @AlphaChars @StringLength(min = 3, max = 30) String nomeProduto,
            @ForAll @BigRange(min = "1", max = "38") BigDecimal valor,
            @ForAll @IntRange(min = 1, max = 50) int quantidade,
            @ForAll @AlphaChars @StringLength(min = 1) String descricao
    ) throws Exception {
        assumeTrue(produtosCadastrados.add(nomeProduto));

        Map<String, Object> payload = Map.of(
                "nome", nomeProduto,
                "valor", valor.toString(),
                "quantidade", quantidade,
                "descricao", descricao,
                "idCategoria", 1,
                "caracteristicasRequest", listaCaracteristicas
        );

        mvc.perform(MockMvcRequestBuilders.post("/v1/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(new AutorizacaoHelper().getAuthorization(tokenManager))
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().is2xxSuccessful());


        MockMultipartFile file1 = new MockMultipartFile("imagens", "img1.jpg", "image/jpeg", new byte[]{1});
        MockMultipartFile file2 = new MockMultipartFile("imagens", "img2.jpg", "image/jpeg", new byte[]{2});


        mvc.perform(multipart("/v1/produtos/1/imagens")
                        .file(file1)
                        .file(file2)
                        .headers(new AutorizacaoHelper().getAuthorization(tokenManager))
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is2xxSuccessful());

    }

    @Test
    @DisplayName("Produto não encontrado")
    void produtoNaoEncontrado() throws Exception {
        MockMultipartFile imagem = new MockMultipartFile("imagens", "imagem.jpg", "image/jpeg", new byte[]{1});

        mvc.perform(multipart("/v1/produtos/999/imagens")
                        .file(imagem)
                        .headers(new AutorizacaoHelper().getAuthorization(tokenManager)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Produto não pertence ao usuário")
    void produtoNaoPertenceAoUsuario() throws Exception {
        MockMultipartFile imagem = new MockMultipartFile("imagens", "imagem.jpg", "image/jpeg", new byte[]{1});

        mvc.perform(multipart("/v1/produtos/2/imagens")
                        .file(imagem)
                        .headers(new AutorizacaoHelper().getAuthorization(tokenManager)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Requisição sem autenticação")
    void requisicaoSemAutenticacao() throws Exception {
        MockMultipartFile imagem = new MockMultipartFile("imagens", "imagem.jpg", "image/jpeg", new byte[]{1});

        mvc.perform(multipart("/v1/produtos/1/imagens")
                        .file(imagem))
                .andExpect(status().isUnauthorized());
    }
}
