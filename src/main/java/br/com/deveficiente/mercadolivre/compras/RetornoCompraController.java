package br.com.deveficiente.mercadolivre.compras;

import br.com.deveficiente.mercadolivre.compartilhado.service.Emails;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/retornos-pagamento")
public class RetornoCompraController {
    private final EntityManager entityManager;

    //1 ICP: Emails
    private final Emails emails;

    @Value("${sistema.url-redirecionamento}")
    private String urlRedirecionamento;

    public RetornoCompraController(EntityManager entityManager, Emails emails) {
        this.entityManager = entityManager;
        this.emails = emails;
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
        //1 ICP: if
        if (pagamento.getStatusPagamento().equals(StatusPagamento.ERRO)) {
            //1 ICP: Compra
            Compra compra = pagamento.getCompra();
            String urlGatewayPagamento = String.format(urlRedirecionamento,
                    compra.getFormaPagmento().toString().toLowerCase(),
                    compra.getId(),
                    compra.getId());
            emails.enviarEmailFalhaPagamentoCliente(compra, urlGatewayPagamento);
        //1 ICP: else
        } else {
            System.out.println("======================================");
            System.out.println("Envia para outros serviços");
            System.out.println("======================================");
        }
        return ResponseEntity.ok().build();
    }
}
