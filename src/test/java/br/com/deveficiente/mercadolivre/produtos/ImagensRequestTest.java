package br.com.deveficiente.mercadolivre.produtos;

import br.com.deveficiente.mercadolivre.produtos.imagens.ImagensRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ImagensRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Deve ser válido quando há imagens com nomes únicos")
    void deveSerValidoComNomesUnicos() {
        List<MultipartFile> arquivos = List.of(
                new MockMultipartFile("file", "img1.jpg", "image/jpeg", new byte[]{1}),
                new MockMultipartFile("file", "img2.jpg", "image/jpeg", new byte[]{2})
        );

        ImagensRequest request = new ImagensRequest(arquivos);
        var violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve falhar quando não há nenhuma imagem")
    void deveFalharComListaVazia() {
        ImagensRequest request = new ImagensRequest(List.of());

        var violations = validator.validate(request);

        assertEquals(1, violations.size());
        String mensagemErro = violations.iterator().next().getMessage();
        assertTrue(mensagemErro.contains("tamanho deve ser entre 1 e"));
    }

    @Test
    @DisplayName("Deve falhar quando há nomes duplicados")
    void deveFalharComNomesDuplicados() {
        List<MultipartFile> arquivos = List.of(
                new MockMultipartFile("file", "img.jpg", "image/jpeg", new byte[]{1}),
                new MockMultipartFile("file", "img.jpg", "image/jpeg", new byte[]{2})
        );

        ImagensRequest request = new ImagensRequest(arquivos);

        var violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("Não pode haver arquivos com nomes duplicados"));
    }

    @Test
    @DisplayName("Deve falhar com lista nula")
    void deveFalharComListaNula() {
        ImagensRequest request = new ImagensRequest(null);

        var violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("não deve ser nulo", violations.iterator().next().getMessage());
    }
}
