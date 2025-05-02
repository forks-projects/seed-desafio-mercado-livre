package br.com.deveficiente.mercadolivre.produtos;

import br.com.deveficiente.mercadolivre.compartilhado.seguranca.UsuarioLogado;
import br.com.deveficiente.mercadolivre.usuarios.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/produtos")
// controller 100% coeso
public class NovoProdutoController {
    private final EntityManager entityManager;

    public NovoProdutoController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    @PostMapping
    //1 ICP: NovoProdutoRequest
    public ResponseEntity<Object> cadastrar(@Valid @RequestBody NovoProdutoRequest novoProdutoRequest, @AuthenticationPrincipal UsuarioLogado usuarioLogado) {
        //1 ICP: Produto
        Usuario usuario = usuarioLogado.getUsuario();
        Produto produto = novoProdutoRequest.toModel(entityManager, usuario);
        entityManager.persist(produto);
        return ResponseEntity.ok().build();
    }
}
