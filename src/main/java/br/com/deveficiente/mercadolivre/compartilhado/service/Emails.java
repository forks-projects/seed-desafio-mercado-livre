package br.com.deveficiente.mercadolivre.compartilhado.service;

import br.com.deveficiente.mercadolivre.compras.Compra;
import br.com.deveficiente.mercadolivre.produtos.perguntas.PerguntaProduto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;


@Service
public class Emails {

    //1 ICP: Mailer
    private final Mailer mailer;

    public Emails(Mailer mailer) {
        this.mailer = mailer;
    }

    //1 ICP: PerguntaProduto
    public void enviarEmailDuvidaCliente(@NotNull @Valid PerguntaProduto pergunta) {
        TemplateEmail templateEmail = new TemplateEmail("<html>...</html>",
                "Nova pergunta...",
                pergunta.getEmailCliente(),
                "novapergunta@nossomercadolivre.com",
                pergunta.getEmailVendedor());
        mailer.send(templateEmail);
    }

    //1 ICP: Compra
    public void enviarEmailCompraVendedor(@NotNull @Valid Compra compra) {
        TemplateEmail templateEmail = new TemplateEmail("<html>...</html>",
                "Nova compra recebida...",
                "Sistema mercado livre",
                "sistema@nossomercadolivre.com",
                compra.getEmailVendedor());
        mailer.send(templateEmail);
    }

}