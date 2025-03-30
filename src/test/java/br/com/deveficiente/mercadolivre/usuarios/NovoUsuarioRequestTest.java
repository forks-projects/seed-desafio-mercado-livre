package br.com.deveficiente.mercadolivre.usuarios;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NovoUsuarioRequestTest {

    @Test
    @DisplayName("Deve mapear request para model")
    void deveMapearRequestParaModel() {
        String loginEsperado = "teste@example.com";
        String senhaEsperada = "senha123";
        NovoUsuarioRequest request = new NovoUsuarioRequest(loginEsperado, senhaEsperada);

        Usuario usuario = request.toModel();

        assertNotNull(usuario);
        assertEquals(request.login(), usuario.getLogin());
        assertEquals(request.senha(), usuario.getSenha());
    }
}