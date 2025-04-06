package br.com.deveficiente.mercadolivre.categorias;

import br.com.deveficiente.mercadolivre.usuarios.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/categorias")
// controller 100% coeso
public class NovaCategoriaController {
    private final EntityManager entityManager;

    public NovaCategoriaController(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    @PostMapping
    //1 ICP: NovoUsuarioRequest
    public ResponseEntity<Usuario> cadastrar(@Valid @RequestBody NovaCategoriaRequest novaCategoriaRequest) {
        //1 ICP: Categoria
        Categoria categoria = novaCategoriaRequest.toModel(entityManager);
        entityManager.persist(categoria);
        return ResponseEntity.ok().build();
    }
}
