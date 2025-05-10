package br.com.deveficiente.mercadolivre.compartilhado.validacao;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class NomeArquivoUnicoValidatorTest {

    @InjectMocks
    private NomeArquivoUnicoValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder;

    private NomeArquivoUnicoLocal nomeArquivoUnicoLocal;

    @BeforeEach
    void setup() {
        nomeArquivoUnicoLocal = new NomeArquivoUnicoLocal();
    }

    @ParameterizedTest(name = "[{index}] {1} válidas")
    @MethodSource("fornecerArquivosComNomesUnicos")
    @DisplayName("Deve ser válido quando arquivos têm nomes únicos")
    void deveSerValidoQuandoArquivosComNomesUnicos(List<MultipartFile> arquivos, String descricaoTeste) {
        boolean resultado = validator.isValid(arquivos, null);
        assertTrue(resultado);
    }

    @Test
    @DisplayName("Deve ser inválido quando arquivos têm nomes duplicados")
    void deveSerInvalidoQuandoArquivosComNomesDuplicados() {
        List<MultipartFile> arquivos = Arrays.asList(
                new MockMultipartFile("file", "imagem.jpg", "image/jpeg", new byte[]{1}),
                new MockMultipartFile("file", "imagem.jpg", "image/jpeg", new byte[]{2}),
                new MockMultipartFile("file", "imagem3.jpg", "image/jpeg", new byte[]{3})
        );

        boolean resultado = validator.isValid(arquivos, context);

        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve ser válido quando lista de arquivos é nula")
    void deveSerValidoQuandoListaArquivosNula() {
        boolean resultado = validator.isValid(null, context);

        assertTrue(resultado);
    }

    @Test
    @DisplayName("Deve ser válido quando lista de arquivos está vazia")
    void deveSerValidoQuandoListaArquivosVazia() {
        boolean resultado = validator.isValid(Collections.emptyList(), context);

        assertTrue(resultado);
    }

    static Stream<Arguments> fornecerArquivosComNomesUnicos() {
        return Stream.of(
                Arguments.of(List.of(
                        new MockMultipartFile("file", "imagem1.jpg", "image/jpeg", new byte[]{1}),
                        new MockMultipartFile("file", "imagem2.jpg", "image/jpeg", new byte[]{2}),
                        new MockMultipartFile("file", "imagem3.jpg", "image/jpeg", new byte[]{3})
                ),
                "Três imagens"),
                Arguments.of(List.of(
                        new MockMultipartFile("file", "foto1.png", "image/png", new byte[]{4}),
                        new MockMultipartFile("file", "foto2.png", "image/png", new byte[]{5})
                ),
                "Duas imagens"
                ),
                Arguments.of(List.of(
                        new MockMultipartFile("file", "imagem.jpg", "imagem/jpg", new byte[]{6})
                ),
                "Uma imagem"
                )
        );
    }
}

class NomeArquivoUnicoLocal implements NomeArquivoUnico {

    @Override
    public String message() {
        return "Não pode haver arquivos com nomes duplicados";
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
        return NomeArquivoUnico.class;
    }
}
