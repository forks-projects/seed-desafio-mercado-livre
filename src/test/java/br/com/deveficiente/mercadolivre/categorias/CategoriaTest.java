package br.com.deveficiente.mercadolivre.categorias;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CategoriaTest {

    @Test
    @DisplayName("Deve adicionar categoria mãe sucesso")
    void deveAdicionarCategoriaMae() {
        Categoria categoriaMae = new Categoria("Categoria mãe");
        Categoria categoria = new Categoria("Categoria");

        categoria.adicionaCategoriaMae(categoriaMae);

        assertNotNull(categoria.getCategoriaMae());
    }

    @Test
    @DisplayName("Não deve adicionar categoria mãe nula")
    void naoDeveAdicionarCategoriaMaeNula() {
        Categoria categoria = new Categoria("Categoria");

        assertThrows(IllegalArgumentException.class, () -> {
            categoria.adicionaCategoriaMae(null);
        });
    }

}