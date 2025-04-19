package br.com.deveficiente.mercadolivre.compartilhado.seguranca;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
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
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class UsuarioAutenticacaoControllerTest {

    @Autowired
    private MockMvc mvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Deve autenticar com sucesso e retornar token JWT")
    void autenticarComSucesso() throws Exception {
        Map<String, Object> payload = Map.of(
                "email", "adriano@email.com",
                "password", "123456"
        );

        mvc.perform(MockMvcRequestBuilders.post("/api/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve falhar autenticação com senha incorreta")
    void autenticarComSenhaIncorreta() throws Exception {
        Map<String, Object> payload = Map.of(
                "email", "adriano@email.com",
                "password", "senhaErrada"
        );

        mvc.perform(MockMvcRequestBuilders.post("/api/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @DisplayName("Deve retornar erro de validação com email vazio ou nulo")
    @ParameterizedTest
    @NullAndEmptySource
    void autenticarComEmailInvalido(String emailInvalido) throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail(emailInvalido);
        request.setPassword("123456");

        mvc.perform(MockMvcRequestBuilders.post("/api/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Deve retornar erro de validação com senha vazia ou nula")
    void autenticarComSenhaInvalida(String senhaInvalida) throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("adriano@email.com");
        request.setPassword(senhaInvalida);

        mvc.perform(MockMvcRequestBuilders.post("/api/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @CsvSource({
            "usuarioemail.com, 123456",
            "usuario@, 123456",
            "@email.com, 123456"
    })
    @DisplayName("Deve falhar para emails com formato inválido")
    void autenticarComEmailMalformado(String emailInvalido, String senha) throws Exception {
        Map<String, Object> payload = Map.of(
                "email", emailInvalido,
                "password", senha
        );

        mvc.perform(MockMvcRequestBuilders.post("/api/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());
    }
}