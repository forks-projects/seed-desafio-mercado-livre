package br.com.deveficiente.mercadolivre.compras;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TarefaNotificaSistemaRanking implements TarefaSucesso {
    private final RestTemplate restTemplate;


    @Value("${sistema.externo.rankings}")
    private String urlSistemaExternoRankings;

    public TarefaNotificaSistemaRanking(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void processar(Pagamento pagamento) {
        //1 ICP: Compra
        Compra compra = pagamento.getCompra();
        //1 ICP: RankingRequest
        RankingRequest rankingRequest = new RankingRequest(compra.getId(), pagamento.getIdVendedor());
        restTemplate.postForEntity(this.urlSistemaExternoRankings, rankingRequest, Void.class);
    }
}
