package br.com.deveficiente.mercadolivre.produtos;

import br.com.deveficiente.mercadolivre.compartilhado.excecao.ResponseErroDTO;
import br.com.deveficiente.mercadolivre.compartilhado.seguranca.UsuarioLogado;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/v1/produtos")
// controller 100% coeso
public class NovaImagemProdutoController {
    private final EntityManager entityManager;
    //1 ICP: Uploader
    private final Uploader uploader;

    public NovaImagemProdutoController(EntityManager entityManager, Uploader uploader) {
        this.entityManager = entityManager;
        this.uploader = uploader;
    }

    @PostMapping("/{idProduto}/imagens")
    @Transactional
    //1 ICP: ImagensRequest
    //1 ICP: UsuarioLogado
    public ResponseEntity<Object> adicionarImagens(@Valid ImagensRequest imagensRequest,
                                                   @PathVariable Long idProduto,
                                                   @AuthenticationPrincipal UsuarioLogado usuarioLogado) {
        Produto produto = entityManager.find(Produto.class, idProduto);
        //1 ICP: if
        if (Objects.isNull(produto)) {
            int statusCode = HttpStatus.NOT_FOUND.value();
            //1 ICP: ResponseErroDTO
            ResponseErroDTO responseErroDTO = new ResponseErroDTO(
                    statusCode,
                    "Produto não encontrado",
                    null);
            return ResponseEntity.status(statusCode).body(responseErroDTO);
        }

        //1 ICP: ImagemProduto
        Set<ImagemProduto> imagens = uploader.enviar(imagensRequest);

        //1 ICP: if
        if(!produto.pertenceA(usuarioLogado.getUsuario())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        produto.adicionarImagens(imagens);
        entityManager.merge(produto);
        return ResponseEntity.ok().build();
    }
}
