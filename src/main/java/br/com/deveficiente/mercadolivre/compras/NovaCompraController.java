package br.com.deveficiente.mercadolivre.compras;

import br.com.deveficiente.mercadolivre.compartilhado.seguranca.UsuarioLogado;
import br.com.deveficiente.mercadolivre.produtos.Produto;
import br.com.deveficiente.mercadolivre.compartilhado.service.Emails;
import br.com.deveficiente.mercadolivre.usuarios.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

// controller 100% coeso
@RestController
@RequestMapping("/v1/compras")
public class NovaCompraController {
    private final EntityManager entityManager;
    // 1 ICP: Emails
    private final Emails emails;

    @Value("${sistema.url-redirecionamento}")
    private String urlRedirecionamento;

    public NovaCompraController(EntityManager entityManager, Emails emails) {
        this.entityManager = entityManager;
        this.emails = emails;
    }

    @PostMapping
    @Transactional
    // 1ICP: NovaCompraRequest
    // 1ICP: UsuarioLogado
    public ResponseEntity<Object> cadastrar(@Valid @RequestBody NovaCompraRequest request,
                                            @AuthenticationPrincipal UsuarioLogado usuarioLogado) {
        // 1 ICP Usuario
        Usuario usuario = usuarioLogado.getUsuario();
        // 1 ICP Produto
        Produto produto = entityManager.find(Produto.class, request.idProduto());
        // 1 ICP if
        if (Objects.isNull(produto)) {
            int statusCode = HttpStatus.NOT_FOUND.value();
            Map<String, Object> informacoesErro = Map.of("status", statusCode, "erro", "Produto não encontrado");
            return ResponseEntity.status(statusCode).body(informacoesErro);
        }
        produto.abaterEstoque(request.quantidade());
        entityManager.merge(produto);
        // 1 ICP: Compra
        Compra compra = request.toModel(usuario, produto);
        entityManager.persist(compra);

        emails.enviarEmailCompraVendedor(compra);

        String urlGatewayPagamento = String.format(urlRedirecionamento,
                request.formaPagamento().toLowerCase(),
                compra.getId(),
                compra.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, urlGatewayPagamento);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
