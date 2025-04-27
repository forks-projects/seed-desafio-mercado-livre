package br.com.deveficiente.mercadolivre.produtos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.*;

class CaracteristicaRequestTest {

    @Test
    @DisplayName("Deve criar uma característica válida")
    void deveCriarCaracteristicaValida() {
        String nome = "Cor";
        String descricao = "Preto";

        CaracteristicaRequest request = new CaracteristicaRequest(nome, descricao);
        Caracteristica caracteristica = request.toModel();

        assertNotNull(caracteristica);
    }

    @DisplayName("Deve lançar exceção se o nome for nulo ou vazio")
    @ParameterizedTest
    @NullAndEmptySource
    void deveLancarExcecaoSeNomeForNuloOuVazio(String nomeInvalido) {
        String descricao = "Descrição válida";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CaracteristicaRequest(nomeInvalido, descricao).toModel()
        );

        assertTrue(exception.getMessage().contains("nome não deve estar em branco"));
    }

    @DisplayName("Deve lançar exceção se a descrição for nula ou vazia")
    @ParameterizedTest
    @NullAndEmptySource
    void deveLancarExcecaoSeDescricaoForNulaOuVazia(String descricaoInvalida) {
        String nome = "Nome válido";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CaracteristicaRequest(nome, descricaoInvalida).toModel()
        );

        assertTrue(exception.getMessage().contains("descrição não deve estar em branco"));
    }
}
