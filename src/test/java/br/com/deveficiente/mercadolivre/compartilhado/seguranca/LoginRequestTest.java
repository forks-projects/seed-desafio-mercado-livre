package br.com.deveficiente.mercadolivre.compartilhado.seguranca;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


class LoginRequestTest {

    @Test
    @DisplayName("Deve construir token de autenticação com email e senha válidos")
    void deveConstruirTokenComEmailESenhaValidos() {
        LoginRequest request = new LoginRequest();
        request.setEmail("usuario@email.com");
        request.setPassword("123456");

        UsernamePasswordAuthenticationToken token = request.build();

        assertNotNull(token);
        assertEquals("usuario@email.com", token.getPrincipal());
        assertEquals("123456", token.getCredentials());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    @DisplayName("Deve lançar exceção para emails nulos, vazios ou em branco")
    void deveLancarExcecaoParaEmailInvalido(String emailInvalido) {
        LoginRequest request = new LoginRequest();
        request.setEmail(emailInvalido);
        request.setPassword("123456");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, request::build);
        assertEquals("Email não pode estar em branco ou nulo", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "usuarioemail.com",      // sem @
            "usuario@",              // sem domínio
            "@email.com",            // sem usuário
            "usuario@email",         // sem TLD
            "usuario@.com",          // domínio inválido
    })
    @DisplayName("Deve lançar exceção para formatos de email inválidos")
    void deveLancarExcecaoParaFormatoDeEmailInvalido(String emailInvalido) {
        LoginRequest request = new LoginRequest();
        request.setEmail(emailInvalido);
        request.setPassword("123456");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, request::build);
        assertEquals("Formato de email inválido", exception.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\n", "\t"})
    @DisplayName("Deve lançar exceção para senhas vazias ou em branco")
    void deveLancarExcecaoParaSenhaInvalida(String senhaInvalida) {
        LoginRequest request = new LoginRequest();
        request.setEmail("usuario@email.com");
        request.setPassword(senhaInvalida);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, request::build);
        assertEquals("Senha não pode estar em branco ou nula", exception.getMessage());
    }
}