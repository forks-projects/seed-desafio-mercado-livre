package br.com.deveficiente.mercadolivre.produtos;

import br.com.deveficiente.mercadolivre.compartilhado.excecao.ResponseErroDTO;
import jakarta.persistence.EntityManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

// controller 100% coeso
@RestController
@RequestMapping("/v1/produtos")
public class DetalhesProdutoController {
    private final EntityManager entityManager;

    public DetalhesProdutoController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @GetMapping("/{idProduto}")
    public ResponseEntity<Object> detalhar(@PathVariable Long idProduto) {
        Produto produto = entityManager.find(Produto.class, idProduto);
        // 1 ICP: if
        if(Objects.isNull(produto)) {
            int statusCode = HttpStatus.NOT_FOUND.value();
            // 1 ICP: ResponseErroDTO
            ResponseErroDTO responseErroDTO = new ResponseErroDTO(statusCode, "Produto não encontrado");
            return ResponseEntity.status(statusCode).body(responseErroDTO);
        }
        // 1 ICP: DetalhesProdutoResponse
        DetalhesProdutoResponse resposta = new DetalhesProdutoResponse(produto);
        return ResponseEntity.ok(resposta);
    }
}
