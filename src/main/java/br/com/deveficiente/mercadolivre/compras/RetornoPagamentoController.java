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

@RestController
@RequestMapping("/v1/retornos-pagamento")
public class RetornoPagamentoController {
    private final EntityManager entityManager;

    // 1ICP: ProcessadorTarefas
    private final ProcessadorTarefas processadorTarefas;

    public RetornoPagamentoController(EntityManager entityManager, ProcessadorTarefas processadorTarefas) {
        this.entityManager = entityManager;
        this.processadorTarefas = processadorTarefas;
    }

    @PostMapping
    @Transactional
    //1 ICP: RetornoPagamentoRequest
    public ResponseEntity<Object> registrarRetorno(@Valid @RequestBody RetornoPagamentoRequest request) {
        //1 ICP: Pagamento
        Pagamento pagamento = request.toModel(entityManager);
        String sqlPagamentoErro = "select p from Pagamento p where p.idPagamento = :pIdPagamento AND p.compra.id = :pIdCompra AND p.statusPagamento = 0";
        Query query = entityManager.createQuery(sqlPagamentoErro);
        query.setParameter("pIdPagamento", request.idPagamento());
        query.setParameter("pIdCompra", request.idCompra());
        //1 ICP: if
        if (!query.getResultList().isEmpty()) {
            Pagamento pagamentoRegistrado = (Pagamento) query.getResultList().get(0);
            pagamentoRegistrado.atualizaStatus(pagamento.getStatusPagamento());
            pagamento = pagamentoRegistrado;
        }
        entityManager.merge(pagamento);
        processadorTarefas.processar(pagamento);
        return ResponseEntity.ok().build();
    }
}
