package br.com.deveficiente.mercadolivre.compras;

import br.com.deveficiente.mercadolivre.compartilhado.service.Emails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TarefaNotificaClienteFalhaPagamento implements TarefaFalha {
    //1 ICP: Emails
    private final Emails emails;

    @Value("${sistema.url-redirecionamento}")
    private String urlRedirecionamento;

    public TarefaNotificaClienteFalhaPagamento(Emails emails) {
        this.emails = emails;
    }

    @Override
    //1 ICP: Pagamento
    public void processar(Pagamento pagamento) {
        //1 ICP: Compra
        Compra compra = pagamento.getCompra();
        String urlGatewayPagamento = String.format(urlRedirecionamento,
                compra.getFormaPagmento().toString().toLowerCase(),
                compra.getId(),
                compra.getId());
        emails.enviarEmailFalhaPagamentoCliente(compra, urlGatewayPagamento);
    }
}
