package br.com.deveficiente.mercadolivre.usuarios;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SenhaLimpaTest {

    @DisplayName("Deve criar senha limpa com sucesso")
    @ParameterizedTest
    @ValueSource(strings = {"123456", "1234567890"})
    void deveCriarSenhaLimpaComSucesso(String senha) {
        SenhaLimpa senhaLimpa = new SenhaLimpa(senha);
        assertNotNull(senhaLimpa);
    }

    @DisplayName("Deve lançar exceção quando senha nula ou vazia")
    @ParameterizedTest
    @NullAndEmptySource
    void deveLancarExcecaoQuandoSenhaNulaOuVazia(String senha) {
        assertThrows(IllegalArgumentException.class, () -> new SenhaLimpa(senha));
    }

    @DisplayName("Deve lançar exceção quando senha menor que 6 caracteres")
    @ParameterizedTest
    @ValueSource(strings = {"12345", "abcd"})
    void deveLancarExcecaoQuandoSenhaMenorQue6Caracteres(String senha) {
        assertThrows(IllegalArgumentException.class, () -> new SenhaLimpa(senha));
    }

}