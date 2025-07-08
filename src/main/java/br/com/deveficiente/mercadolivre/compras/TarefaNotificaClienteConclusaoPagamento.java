package br.com.deveficiente.mercadolivre.compras;

import br.com.deveficiente.mercadolivre.compartilhado.service.Emails;
import org.springframework.stereotype.Component;

@Component
public class TarefaNotificaClienteConclusaoPagamento implements TarefaSucesso {
    //1 ICP: Emails
    private final Emails emails;

    public TarefaNotificaClienteConclusaoPagamento(Emails emails) {
        this.emails = emails;
    }

    @Override
    //1 ICP: Pagamento
    public void processar(Pagamento pagamento) {
        emails.enviarEmailClienteConclusaoPagamento(pagamento.getCompra());
    }
}
