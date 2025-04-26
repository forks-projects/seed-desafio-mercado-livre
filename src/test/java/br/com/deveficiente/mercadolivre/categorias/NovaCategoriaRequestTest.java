package br.com.deveficiente.mercadolivre.categorias;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NovaCategoriaRequestTest {

    @Mock
    private EntityManager entityManager;

    @Test
    @DisplayName("Deve criar categoria sem categoria mãe")
    void deveCriarCategoriaSemCategoriaMae() {
        String nome = "Eletrônicos";
        NovaCategoriaRequest request = new NovaCategoriaRequest(nome, null);

        Categoria categoria = request.toModel(entityManager);

        assertNotNull(categoria);
        assertEquals(request.nome(), categoria.getNome());
        assertNull(categoria.getCategoriaMae());
    }

    @Test
    @DisplayName("Deve criar categoria com categoria mãe existente")
    void deveCriarCategoriaComCategoriaMaeExistente() {
        String nome = "Smartphones";
        Long idCategoriaMae = 1L;
        NovaCategoriaRequest request = new NovaCategoriaRequest(nome, idCategoriaMae);
        Categoria categoriaMae = new Categoria("Tecnologia");
        when(entityManager.find(Categoria.class, idCategoriaMae)).thenReturn(categoriaMae);

        Categoria categoria = request.toModel(entityManager);

        assertNotNull(categoria);
        assertEquals(request.nome(), categoria.getNome());
        assertEquals(categoriaMae, categoria.getCategoriaMae());
    }

    @Test
    @DisplayName("Deve lançar exceção se categoria mãe não for encontrada")
    void deveLancarExcecaoSeCategoriaMaeNaoForEncontrada() {
        String nome = "Smartphones";
        Long idCategoriaMae = 1L;
        NovaCategoriaRequest request = new NovaCategoriaRequest(nome, idCategoriaMae);
        when(entityManager.find(Categoria.class, idCategoriaMae)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> request.toModel(entityManager)
        );

        assertEquals("Categoria mãe não encontrada", exception.getMessage());
    }

    @DisplayName("Deve lançar exceção ao tentar criar categoria com nome invalido")
    @ParameterizedTest
    @NullAndEmptySource
    void deveLancarExcecaoQuandoNomeForNulo(String nome) {
        NovaCategoriaRequest request = new NovaCategoriaRequest(nome, null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> request.toModel(entityManager)
        );

        assertEquals("Nome não pode estar em branco", exception.getMessage());
    }
}