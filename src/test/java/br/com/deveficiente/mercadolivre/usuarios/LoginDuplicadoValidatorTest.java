package br.com.deveficiente.mercadolivre.usuarios;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginDuplicadoValidatorTest {

    @InjectMocks
    private LoginDuplicadoValidator loginDuplicadoValidator;

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;

    private NovoUsuarioRequest novoUsuarioRequest;

    @BeforeEach
    void setUp() {
        novoUsuarioRequest = new NovoUsuarioRequest("joao", "senha123");
    }

    @Test
    @DisplayName("Deve validar login quando não estiver cadastrado")
    void deveValidarLoginQuandoNaoEstiverCadastrado() {
        Errors errors = new BeanPropertyBindingResult(novoUsuarioRequest.toModel(), "teste");
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.emptyList());

        loginDuplicadoValidator.validate(novoUsuarioRequest, errors);

        assertFalse(errors.hasErrors());
    }

    @Test
    @DisplayName("Deve rejeitar login quando já estiver cadastrado")
    void deveRejeitarLoginQuandoJaEstiverCadastrado() {
        Errors errors = new BeanPropertyBindingResult(novoUsuarioRequest.toModel(), "teste");
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        Usuario usuario = new Usuario(novoUsuarioRequest.login(), new SenhaLimpa(novoUsuarioRequest.senha()));
        when(query.getResultList()).thenReturn(List.of(usuario));

        loginDuplicadoValidator.validate(novoUsuarioRequest, errors);

        assertTrue(errors.hasErrors());
    }

    @Test
    @DisplayName("Não deve validar caso já existam outros erros no formulário")
    void naoDeveValidarCasoJaExistamOutrosErros() {
        NovoUsuarioRequest usuarioRequestInvalido = new NovoUsuarioRequest("logininvalido", "123");
        Errors errors = new BeanPropertyBindingResult(usuarioRequestInvalido, "teste");
        errors.rejectValue("login", "null", "campo inválido");

        loginDuplicadoValidator.validate(usuarioRequestInvalido, errors);

        assertTrue(errors.hasErrors());
    }
}