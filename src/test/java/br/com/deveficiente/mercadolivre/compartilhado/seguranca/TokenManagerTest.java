package br.com.deveficiente.mercadolivre.compartilhado.seguranca;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenManagerTest {

    private TokenManager tokenManager;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setup() {
        tokenManager = new TokenManager();
        String rawSecret = "a-very-secret-key-that-is-at-least-32-bytes-long";
        ReflectionTestUtils.setField(tokenManager, "rawSecret", rawSecret);
        ReflectionTestUtils.setField(tokenManager, "expirationInMillis", 3600000L);
        tokenManager.init();
    }

    @Test
    @DisplayName("Deve gerar um token JWT válido e conseguir extrair o username")
    void testGenerateAndValidateToken() {
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("usuario@email.com");

        String token = tokenManager.generateToken(authentication);

        assertThat(token).isNotBlank();
        assertThat(tokenManager.isValid(token)).isTrue();
        assertThat(tokenManager.getUserName(token)).isEqualTo("usuario@email.com");
    }

    @Test
    @DisplayName("Deve retornar falso para token inválido")
    void testInvalidToken() {
        String tokenInvalido = "abc.def.ghi";

        boolean valido = tokenManager.isValid(tokenInvalido);

        assertThat(valido).isFalse();
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar extrair username de token inválido")
    void testGetUsernameFromInvalidToken() {
        String tokenInvalido = "abc.def.ghi";

        assertThrows(JwtException.class, () -> {
            tokenManager.getUserName(tokenInvalido);
        });
    }
}
