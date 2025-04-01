package br.com.deveficiente.mercadolivre.usuarios;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NovoUsuarioRequestTest {

    @Test
    @DisplayName("Deve mapear request para model")
    void deveMapearRequestParaModel() {
        String loginEsperado = "teste@example.com";
        String senhaEsperada = "senha123";
        NovoUsuarioRequest request = new NovoUsuarioRequest(loginEsperado, senhaEsperada);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Usuario usuario = request.toModel();

        assertNotNull(usuario);
        assertEquals(request.login(), usuario.getLogin());
        boolean isSenhasIguais = encoder.matches(request.senha(), usuario.getSenha());
        assertTrue(isSenhasIguais);
    }
}