package br.com.deveficiente.mercadolivre.categorias;

import br.com.deveficiente.mercadolivre.compartilhado.seguranca.AutorizacaoHelper;
import br.com.deveficiente.mercadolivre.compartilhado.seguranca.TokenManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.jqwik.api.ForAll;
import net.jqwik.api.Label;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.AlphaChars;
import net.jqwik.api.constraints.LongRange;
import net.jqwik.api.constraints.StringLength;
import net.jqwik.spring.JqwikSpringSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@JqwikSpringSupport
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class NovaCategoriaControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private TokenManager tokenManager;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static Set<String> categoriaCadastrada = new HashSet<>();

    @Property(tries = 10)
    @Label("Cadastrar categoria sem categoria mãe")
    void cadastrarCategoriaSemMae(
            @ForAll @AlphaChars @StringLength(min = 3, max = 30) String nome
    ) throws Exception {
        assumeTrue(categoriaCadastrada.add(nome));
        Map<String, Object> payloadMap = Map.of("nome", nome);
        String payload = objectMapper.writeValueAsString(payloadMap);

        mvc.perform(MockMvcRequestBuilders.post("/v1/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(new AutorizacaoHelper().getAuthorization(tokenManager))
                        .content(payload))
                .andExpect(status().is2xxSuccessful());
    }

    @DisplayName("Cadastrar categoria com nome inválido")
    @ParameterizedTest
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    void cadastrarCategoriaComNomeInvalido(
        String nomeInvalido
    ) throws Exception {
        Map<String, Object> payloadMap = Map.of("nome", nomeInvalido);
        String payload = objectMapper.writeValueAsString(payloadMap);

        mvc.perform(MockMvcRequestBuilders.post("/v1/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(new AutorizacaoHelper().getAuthorization(tokenManager))
                        .content(payload))
                .andExpect(status().is4xxClientError());
    }

    @Property(tries = 10)
    @Label("Cadastrar categoria com idCategoriaMae inexistente")
    void cadastrarCategoriaComMaeInexistente(
            @ForAll @AlphaChars @StringLength(min = 3, max = 30) String nome,
            @ForAll @LongRange(min = 3, max = 30) Long idCategoriaMaeNaoEncontrada
    ) throws Exception {
        Map<String, Object> payloadMap = Map.of(
                "nome", nome,
                "idCategoriaMae", idCategoriaMaeNaoEncontrada
        );

        String payload = objectMapper.writeValueAsString(payloadMap);

        mvc.perform(MockMvcRequestBuilders.post("/v1/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(new AutorizacaoHelper().getAuthorization(tokenManager))
                        .content(payload))
                .andExpect(status().is4xxClientError());
    }

    @Property(tries = 10)
    @Label("Cadastrar categoria com mãe existente")
    void cadastrarCategoriaComMaeExistente(
            @ForAll @AlphaChars @StringLength(min = 4, max = 20) String nomeCategoriaFilha
    ) throws Exception {
        assumeTrue(categoriaCadastrada.add(nomeCategoriaFilha));
        Map<String, Object> payloadCategoriaMae = Map.of("nome", "Categoria mãe");
        mvc.perform(MockMvcRequestBuilders.post("/v1/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(new AutorizacaoHelper().getAuthorization(tokenManager))
                .content(objectMapper.writeValueAsString(payloadCategoriaMae)));

        Map<String, Object> payloadMap = Map.of(
                "nome", nomeCategoriaFilha,
                "idCategoriaMae", 1
        );

        String payload = objectMapper.writeValueAsString(payloadMap);

        mvc.perform(MockMvcRequestBuilders.post("/v1/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(new AutorizacaoHelper().getAuthorization(tokenManager))
                        .content(payload))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Acesso não autorizado para cadastrar categoria sem autenticação")
    void acessoNaoAutorizadoParaCadastrarCategoriaSemAutenticacao() throws Exception {
        Map<String, Object> payload = Map.of("nome", "NovaCategoria");

        mvc.perform(MockMvcRequestBuilders.post("/v1/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isUnauthorized());
    }
}