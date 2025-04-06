package br.com.deveficiente.mercadolivre.compartilhado.validacao;

import br.com.deveficiente.mercadolivre.categorias.Categoria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExisteIdValidatorTest {

    @InjectMocks
    private ExisteIdValidator existeIdValidator;

    @Mock
    private EntityManager entityManager;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private Query query;

    private ExistedIdLocal existedIdLocal;

    @BeforeEach
    void setup() throws NoSuchFieldException, IllegalAccessException {
        existedIdLocal = new ExistedIdLocal();
    }

    @Test
    @DisplayName("Deve ser válido quando id não está cdastrado")
    void deveSerValidoQuandoIdNaoEstaCadastradi() {
        when(entityManager.createQuery(contains("SELECT e FROM "))).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.emptyList());
        existeIdValidator.initialize(existedIdLocal);

        boolean resultado = existeIdValidator.isValid("idCategoriaMae", context);

        assertFalse(resultado);
    }


    @Test
    @DisplayName("Deve ser válido quando id nulo")
    void deveSerValidoQuandoIdNulo() {
        existeIdValidator.initialize(existedIdLocal);

        boolean resultado = existeIdValidator.isValid(null, context);

        assertTrue(resultado);
    }

    @Test
    @DisplayName("Deve ser inválido quando id existe")
    void deveSerInvalidoQuandoIdExiste() {
        Categoria categoria = new Categoria("Tecnologia");

        when(entityManager.createQuery(contains("SELECT e FROM "))).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(categoria));
        existeIdValidator.initialize(existedIdLocal);

        boolean resultado = existeIdValidator.isValid("idCategoriaMae", context);

        assertTrue(resultado);
    }

    @Test
    @DisplayName("Deve ser inválido quando existir mais de um id")
    void deveSerInvalidoQuandoExistirMaisDeUmId() {
        Categoria categoria1 = new Categoria("Tecnologia");
        Categoria categoria2 = new Categoria("Programação");

        when(entityManager.createQuery(contains("SELECT e FROM "))).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(categoria1, categoria2));
        existeIdValidator.initialize(existedIdLocal);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            existeIdValidator.isValid("idCategoriaMae", context);
        });

        assertTrue(exception.getMessage().contains("aconteceu algo estranho"));
    }
}

class ExistedIdLocal implements ExisteId {

    private String nomeCampo;

    public ExistedIdLocal() {
        this.nomeCampo = "idCategoriaMae";
    }

    @Override
    public String message() {
        return "";
    }

    @Override
    public Class<?>[] groups() {
        return new Class[0];
    }

    @Override
    public Class<? extends Payload>[] payload() {
        return new Class[0];
    }

    @Override
    public String nomeDoCampo() {
        return nomeCampo;
    }

    @Override
    public Class<?> classeDaEntidade() {
        return Categoria.class;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}