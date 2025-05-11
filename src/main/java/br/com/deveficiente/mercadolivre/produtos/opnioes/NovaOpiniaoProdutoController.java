package br.com.deveficiente.mercadolivre.produtos.opnioes;

import br.com.deveficiente.mercadolivre.compartilhado.excecao.ResponseErroDTO;
import br.com.deveficiente.mercadolivre.compartilhado.seguranca.UsuarioLogado;
import br.com.deveficiente.mercadolivre.produtos.Produto;
import br.com.deveficiente.mercadolivre.usuarios.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/v1/produtos")
public class NovaOpiniaoProdutoController {
    private final EntityManager entityManager;

    public NovaOpiniaoProdutoController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    //1 ICP OpniaoProdutoRequest
    @PostMapping("/{idProduto}/opinioes")
    @Transactional
    public ResponseEntity<Object> cadastrar(@Valid @RequestBody OpiniaoProdutoRequest request,
                                            @PathVariable Long idProduto,
                                            @AuthenticationPrincipal UsuarioLogado usuarioLogado) {
        // 1 ICP Produto
        Produto produto = entityManager.find(Produto.class, idProduto);
        if (Objects.isNull(produto)) {
            // 1 ICP ResponseErroDTO
            ResponseErroDTO responseErroDTO = new ResponseErroDTO(HttpStatus.NOT_FOUND.value(), "Produto não encontrado");
            new ResponseEntity<>(responseErroDTO, HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseErroDTO);
        }
        // 1 ICP Usuario
        Usuario usuario = usuarioLogado.getUsuario();
        OpiniaoProduto opiniaoProduto = request.toModel(produto, usuario);
        entityManager.persist(opiniaoProduto);
        return ResponseEntity.ok().build();
    }

}
