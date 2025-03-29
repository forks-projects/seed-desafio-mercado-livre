package bv.com.deveficiente.mercadolivre.usuarios;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/usuarios")
public class NovoUsuarioController {
    private final EntityManager entityManager;

    public NovoUsuarioController(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    @PostMapping
    public ResponseEntity<Usuario> cadastrar(@Valid @RequestBody NovoUsuarioRequest novoUsuarioRequest) {
        Usuario usuario = novoUsuarioRequest.toModel();
        entityManager.persist(usuario);
        return ResponseEntity.ok().build();
    }
}
