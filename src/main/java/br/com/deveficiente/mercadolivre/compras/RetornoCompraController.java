package br.com.deveficiente.mercadolivre.compras;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/v1/retornos-pagamento")
public class RetornoCompraController {
    private final EntityManager entityManager;

    public RetornoCompraController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Object> registrarRetorno(@Valid @RequestBody RetornoPagamentoRequest request) {
        Pagamento pagamento = request.toModel(entityManager);
        Query query = entityManager.createQuery("select p from Pagamento p where p.compra.id = :pIdCompra");
        query.setParameter("pIdCompra", request.idCompra());
        if (!query.getResultList().isEmpty()) {
            throw new IllegalArgumentException("Compra já está paga");
        }
        entityManager.persist(pagamento);
        return ResponseEntity.ok().build();
    }
}
