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
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/v1/retornos-pagamento")
public class RetornoPagamentoController {
    private final EntityManager entityManager;

    //1 ICP: Emails
    private final Emails emails;

    private final RestTemplate restTemplate;

    @Value("${sistema.url-redirecionamento}")
    private String urlRedirecionamento;

    @Value("${sistema.externo.notas-fiscais}")
    private String urlSistemaExternoNotasFiscais;

    @Value("${sistema.externo.rankings}")
    private String urlSistemaExternoRankings;

    public RetornoPagamentoController(EntityManager entityManager, Emails emails, RestTemplate restTemplate) {
        this.entityManager = entityManager;
        this.emails = emails;
        this.restTemplate = restTemplate;
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

            //1 ICP: NotaFiscalRequest
            NotaFiscalRequest notaFiscalRequest = new NotaFiscalRequest(request.idCompra(), pagamento.getIdCliente());
            restTemplate.postForEntity(this.urlSistemaExternoNotasFiscais, notaFiscalRequest, Void.class);

            //1 ICP: RankingRequest
            RankingRequest rankingRequest = new RankingRequest(request.idCompra(), pagamento.getIdVendedor());
            restTemplate.postForEntity(this.urlSistemaExternoRankings, rankingRequest, Void.class);
        }
        return ResponseEntity.ok().build();
    }
}
