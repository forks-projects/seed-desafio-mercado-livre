package br.com.deveficiente.mercadolivre.produtos.perguntas;

import br.com.deveficiente.mercadolivre.compras.Compra;
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
        mailer.send("<html>...</html>",
                "Nova pergunta...",
                pergunta.getEmailCliente(),
                "novapergunta@nossomercadolivre.com",
                pergunta.getEmailVendedor());
    }

    //1 ICP: Compra
    public void enviarEmailCompraVendedor(@NotNull @Valid Compra compra) {
        mailer.send("<html>...</html>",
                "Nova compra recebida...",
                "Sistema mercado livre",
                "sistema@nossomercadolivre.com",
                compra.getEmailVendedor());
    }

}