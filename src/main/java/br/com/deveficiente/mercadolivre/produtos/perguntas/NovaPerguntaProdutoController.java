package br.com.deveficiente.mercadolivre.produtos.perguntas;

import br.com.deveficiente.mercadolivre.compartilhado.excecao.ResponseErroDTO;
import br.com.deveficiente.mercadolivre.compartilhado.seguranca.UsuarioLogado;
import br.com.deveficiente.mercadolivre.compartilhado.service.Emails;
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
// controller 100% coeso
public class NovaPerguntaProdutoController {
    private final EntityManager entityManager;

    // 1ICP: Emails
    private final Emails emailService;

    public NovaPerguntaProdutoController(EntityManager entityManager, Emails emailService) {
        this.entityManager = entityManager;
        this.emailService = emailService;
    }

    @PostMapping("/{idProduto}/perguntas")
    @Transactional
    // 1 ICP: NovaPerguntaRequest
    // 1 ICP: UsuarioLogado
    public ResponseEntity<Object> cadastrar(@Valid @RequestBody NovaPerguntaRequest novaPerguntaRequest,
                                            @PathVariable Long idProduto,
                                            @AuthenticationPrincipal UsuarioLogado usuarioLogado) {
        // 1 ICP: Produto
        Produto produto = entityManager.find(Produto.class, idProduto);
        // 1 ICP: if
        if(Objects.isNull(produto)) {
            int statusCode = HttpStatus.NOT_FOUND.value();
            // 1 ICP: ResponseErroDTO
            ResponseErroDTO responseErroDTO = new ResponseErroDTO(statusCode, "Produto não encontrado");
            return ResponseEntity.status(statusCode).body(responseErroDTO);
        }

        // 1 ICP: Usuario
        Usuario usuario = usuarioLogado.getUsuario();

        // 1 ICP: PerguntaProduto
        PerguntaProduto perguntaProduto = novaPerguntaRequest.toModel(produto, usuario);
        entityManager.persist(perguntaProduto);

        emailService.enviarEmailDuvidaCliente(perguntaProduto);
        return ResponseEntity.ok().build();
    }
}
