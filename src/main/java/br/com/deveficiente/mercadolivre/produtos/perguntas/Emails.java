package br.com.deveficiente.mercadolivre.produtos.perguntas;

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

}