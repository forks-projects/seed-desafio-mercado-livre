package br.com.deveficiente.mercadolivre.usuarios;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jqwik.api.ForAll;
import net.jqwik.api.Label;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.AlphaChars;
import net.jqwik.api.constraints.StringLength;
import net.jqwik.spring.JqwikSpringSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assumptions.assumeTrue;


@SpringBootTest
@JqwikSpringSupport
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class NovoUsuarioControllerTest {

    @Autowired
    private MockMvc mvc;

    private static Set<String> usuarioCadastrado = new HashSet<>();

    @Property(tries = 10)
    @Label("Cadastrar usuário")
    void cadastrarUsuario(
            @ForAll @AlphaChars @StringLength(min = 5, max = 50) String prefixoLogin,
            @ForAll @AlphaChars @StringLength(min = 6, max = 15) String senha
    ) throws Exception {
        String login = prefixoLogin.concat("@email.com");
        assumeTrue(usuarioCadastrado.add(login));

        Map<String, String> payloadMap = Map.of("login", login, "senha", senha);
        String payload = new ObjectMapper().writeValueAsString(payloadMap);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload);

        mvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        mvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Property(tries = 10)
    @Label("Cadastrar usuário com email inválido")
    void cadastrarUsuarioComEmailInvalido(
            @ForAll @AlphaChars @StringLength(min = 5, max = 255) String loginInvalido,
            @ForAll @AlphaChars @StringLength(min = 6, max = 15) String senha
    ) throws Exception {

        Map<String, String> payloadMap = Map.of("login", loginInvalido, "senha", senha);
        String payload = new ObjectMapper().writeValueAsString(payloadMap);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload);

        mvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Property(tries = 10)
    @Label("Cadastrar usuário com senha inválida")
    void cadastrarUsuarioComSenhaInvalida(
            @ForAll @AlphaChars @StringLength(min = 5, max = 245) String prefixoLogin,
            @ForAll @AlphaChars @StringLength(max = 5) String senhaInvalida
    ) throws Exception {
        String login = prefixoLogin.concat("@email.com");
        assumeTrue(usuarioCadastrado.add(login));

        Map<String, String> payloadMap = Map.of("login", login, "senha", senhaInvalida);
        String payload = new ObjectMapper().writeValueAsString(payloadMap);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload);

        mvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
}
