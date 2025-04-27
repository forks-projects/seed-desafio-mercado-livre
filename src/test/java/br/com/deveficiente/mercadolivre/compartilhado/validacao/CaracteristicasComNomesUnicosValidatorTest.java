package br.com.deveficiente.mercadolivre.compartilhado.validacao;

import br.com.deveficiente.mercadolivre.compartilhado.databuilders.CaracteristicaRequestBuilder;
import br.com.deveficiente.mercadolivre.produtos.CaracteristicaRequest;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class CaracteristicasComNomesUnicosValidatorTest {

    @InjectMocks
    private CaracteristicasComNomesUnicosValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    private CaracteristicasComNomesUnicosLocal caracteristicasComNomesUnicosLocal;

    @BeforeEach
    void setup() {
        caracteristicasComNomesUnicosLocal = new CaracteristicasComNomesUnicosLocal();
    }

    @Test
    @DisplayName("Deve ser válido quando características têm nomes únicos")
    void deveSerValidoQuandoCaracteristicasComNomesUnicos() {
        List<CaracteristicaRequest> caracteristicas = List.of(
                CaracteristicaRequestBuilder.umaCaracteristica().comNome("cor").comDescricao("preto").build(),
                CaracteristicaRequestBuilder.umaCaracteristica().comNome("peso").comDescricao("200g").build(),
                CaracteristicaRequestBuilder.umaCaracteristica().comNome("material").comDescricao("metal").build()
        );

        boolean resultado = validator.isValid(caracteristicas, context);

        assertTrue(resultado);
    }

    @Test
    @DisplayName("Deve ser inválido quando características têm nomes repetidos")
    void deveSerInvalidoQuandoCaracteristicasComNomesDuplicados() {
        List<CaracteristicaRequest> caracteristicas = List.of(
                CaracteristicaRequestBuilder.umaCaracteristica().comNome("cor").comDescricao("preto").build(),
                CaracteristicaRequestBuilder.umaCaracteristica().comNome("cor").comDescricao("200g").build(),
                CaracteristicaRequestBuilder.umaCaracteristica().comNome("material").comDescricao("metal").build()
        );

        boolean resultado = validator.isValid(caracteristicas, context);

        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve ser válido quando lista de características é nula")
    void deveSerValidoQuandoListaCaracteristicasNula() {
        boolean resultado = validator.isValid(null, context);

        assertTrue(resultado);
    }

    @Test
    @DisplayName("Deve ser válido quando lista de características está vazia")
    void deveSerValidoQuandoListaCaracteristicasVazia() {
        boolean resultado = validator.isValid(List.of(), context);

        assertTrue(resultado);
    }
}

class CaracteristicasComNomesUnicosLocal implements CaracteristicasComNomesUnicos {

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
    public Class<? extends Annotation> annotationType() {
        return CaracteristicasComNomesUnicos.class;
    }
}
