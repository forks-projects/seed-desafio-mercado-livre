package br.com.deveficiente.mercadolivre.compras;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TarefaNotificaSistemaNotaFiscal implements TarefaSucesso {
    private final RestTemplate restTemplate;

    @Value("${sistema.externo.notas-fiscais}")
    private String urlSistemaExternoNotasFiscais;

    public TarefaNotificaSistemaNotaFiscal(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void processar(Pagamento pagamento) {
        //1 ICP: Compra
        Compra compra = pagamento.getCompra();
        //1 ICP: NotaFiscalRequest
        NotaFiscalRequest notaFiscalRequest = new NotaFiscalRequest(compra.getId(), pagamento.getIdCliente());
        restTemplate.postForEntity(this.urlSistemaExternoNotasFiscais, notaFiscalRequest, Void.class);
    }
}
