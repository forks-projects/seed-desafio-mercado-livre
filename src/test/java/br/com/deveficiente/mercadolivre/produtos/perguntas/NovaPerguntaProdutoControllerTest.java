package br.com.deveficiente.mercadolivre.produtos.perguntas;

import br.com.deveficiente.mercadolivre.compartilhado.seguranca.AutorizacaoHelper;
import br.com.deveficiente.mercadolivre.compartilhado.seguranca.TokenManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.jqwik.api.*;
import net.jqwik.api.constraints.AlphaChars;
import net.jqwik.api.constraints.StringLength;
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
class NovaPerguntaProdutoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TokenManager tokenManager;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Property(tries = 10)
    @Label("Cadastro de pergunta com sucesso")
    void cadastroPerguntaComSucesso(
            @ForAll @AlphaChars @StringLength(min = 1, max = 255) String titulo
    ) throws Exception {
        Map<String, Object> payload = Map.of("titulo", titulo);

        mvc.perform(MockMvcRequestBuilders.post("/v1/produtos/1/perguntas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(new AutorizacaoHelper().getAuthorization(tokenManager))
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Cadastro de pergunta com produto inexistente")
    void cadastroPerguntaProdutoInexistente() throws Exception {
        Map<String, Object> payload = Map.of("titulo", "Produto ainda está disponível?");

        mvc.perform(MockMvcRequestBuilders.post("/v1/produtos/999/perguntas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(new AutorizacaoHelper().getAuthorization(tokenManager))
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro").value("Produto não encontrado"));
    }

    @Test
    @DisplayName("Cadastro de pergunta sem autenticação")
    void cadastroPerguntaSemAutenticacao() throws Exception {
        Map<String, Object> payload = Map.of("titulo", "Tem garantia?");

        mvc.perform(MockMvcRequestBuilders.post("/v1/produtos/1/perguntas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isUnauthorized());
    }

}
