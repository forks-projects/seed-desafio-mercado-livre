package br.com.deveficiente.mercadolivre.usuarios;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/usuarios")
// controller 100% coeso
public class NovoUsuarioController {
    private final EntityManager entityManager;
    //1 ICP: LoginDuplicadoValidator
    private final LoginDuplicadoValidator loginDuplicadoValidator;

    @InitBinder
    public void init(WebDataBinder binder) {
        binder.addValidators(loginDuplicadoValidator);
    }

    public NovoUsuarioController(final EntityManager entityManager, LoginDuplicadoValidator loginDuplicadoValidator) {
        this.entityManager = entityManager;
        this.loginDuplicadoValidator = loginDuplicadoValidator;
    }

    @Transactional
    @PostMapping
    //1 ICP: NovoUsuarioRequest
    public ResponseEntity<Usuario> cadastrar(@Valid @RequestBody NovoUsuarioRequest novoUsuarioRequest) {
        //1 ICP: Usuario
        Usuario usuario = novoUsuarioRequest.toModel();
        entityManager.persist(usuario);
        return ResponseEntity.ok().build();
    }
}
