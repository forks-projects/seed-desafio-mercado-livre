package br.com.deveficiente.mercadolivre.produtos.opnioes;

import br.com.deveficiente.mercadolivre.compartilhado.seguranca.AutorizacaoHelper;
import br.com.deveficiente.mercadolivre.compartilhado.seguranca.TokenManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.jqwik.api.*;
import net.jqwik.api.constraints.*;
import net.jqwik.spring.JqwikSpringSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@JqwikSpringSupport
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class NovaOpiniaoProdutoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TokenManager tokenManager;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Property(tries = 10)
    @Label("Cadastro de opinião com sucesso")
    void cadastroOpiniaoComSucesso(
            @ForAll @IntRange(min = 1, max = 5) int nota,
            @ForAll @AlphaChars @StringLength(min = 3, max = 30) String titulo,
            @ForAll @AlphaChars @StringLength(min = 3, max = 500) String descricao
    ) throws Exception {
        Map<String, Object> payload = Map.of(
                "nota", nota,
                "titulo", titulo,
                "descricao", descricao
        );

        mvc.perform(MockMvcRequestBuilders.post("/v1/produtos/1/opinioes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(new AutorizacaoHelper().getAuthorization(tokenManager))
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Cadastro de opinião com produto inexistente")
    void cadastroOpiniaoProdutoInexiste() throws Exception {
        Map<String, Object> payload = Map.of(
                "nota", 4,
                "titulo", "Bom",
                "descricao", "Gostei bastante"
        );

        mvc.perform(MockMvcRequestBuilders.post("/v1/produtos/999/opinioes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(new AutorizacaoHelper().getAuthorization(tokenManager))
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro").value("Produto não encontrado"));
    }

    @Test
    @DisplayName("Cadastro de opinião sem autenticação")
    void cadastroOpiniaoSemAutenticacao() throws Exception {
        Map<String, Object> payload = Map.of(
                "nota", 3,
                "titulo", "Regular",
                "descricao", "Atende o básico"
        );

        mvc.perform(MockMvcRequestBuilders.post("/v1/produtos/1/opinioes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isUnauthorized());
    }

}
