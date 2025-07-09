package br.com.deveficiente.mercadolivre.produtos.caracteristicas;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CaracteristicaResponseTest {
    @Test
    @DisplayName("Deve criar caracteristica response passando os valores no construtor")
    void deveCriarCaracteristicaResponsePassandoValoresConstrutor() {
        Long idCategoria = 100L;
        String nomeCategoria = "Sistema Operacional";
        String descricao = "Android";

        CaracteristicaResponse caracteristicaResponse = new CaracteristicaResponse(idCategoria,
                nomeCategoria,
                descricao);

        assertEquals(caracteristicaResponse.id(), idCategoria);
        assertEquals(caracteristicaResponse.nome(), nomeCategoria);
        assertEquals(caracteristicaResponse.descricao(), descricao);
    }

    @Test
    @DisplayName("Deve criar caracteristica response passando uma categoria")
    void deveCriarCaracteristicaResponsePassandoUmaCategoria() {
        String nomeCategoria = "Sistema Operacional";
        String descricao = "Android";
        Caracteristica caracteristica = new Caracteristica(nomeCategoria, descricao);

        CaracteristicaResponse caracteristicaResponse = new CaracteristicaResponse(caracteristica);

        assertEquals(caracteristicaResponse.nome(), nomeCategoria);
        assertEquals(caracteristicaResponse.descricao(), descricao);
    }
}