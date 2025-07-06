package br.com.deveficiente.mercadolivre.compartilhado.service;

import br.com.deveficiente.mercadolivre.compras.Compra;
import br.com.deveficiente.mercadolivre.produtos.Produto;
import br.com.deveficiente.mercadolivre.produtos.perguntas.PerguntaProduto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class Emails {

    //1 ICP: Mailer
    private final Mailer mailer;

    public Emails(Mailer mailer) {
        this.mailer = mailer;
    }

    //1 ICP: PerguntaProduto
    // dica (anotações) para quem usar o construtor
    public void enviarEmailDuvidaCliente(@NotNull @Valid PerguntaProduto pergunta) {
        //1 ICP: TemplateEmail
        TemplateEmail templateEmail = new TemplateEmail("<html>...</html>",
                "Nova pergunta...",
                pergunta.getEmailCliente(),
                "novapergunta@nossomercadolivre.com",
                pergunta.getEmailVendedor());
        mailer.send(templateEmail);
    }

    //1 ICP: Compra
    // dica (anotações) para quem usar o construtor
    public void enviarEmailCompraVendedor(@NotNull @Valid Compra compra) {
        TemplateEmail templateEmail = new TemplateEmail("<html>...</html>",
                "Nova compra recebida...",
                "Sistema mercado livre",
                "sistema@nossomercadolivre.com",
                compra.getEmailCliente());
        mailer.send(templateEmail);
    }

    // dica (anotações) para quem usar o construtor
    public void enviarEmailFalhaPagamentoCliente(@NotNull @Valid Compra compra, @NotBlank String urlGatewayPagamento) {
        TemplateEmail templateEmail = new TemplateEmail(
                "Falha no pagamento. Acesse: " + urlGatewayPagamento,
                "Falha no pagamento...",
                "Sistema mercado livre",
                "sistema@nossomercadolivre.com",
                compra.getEmailVendedor());
        mailer.send(templateEmail);
    }

    // dica (anotações) para quem usar o construtor
    public void enviarEmailClienteConclusaoPagamento(@NotNull @Valid Compra compra) {
        Produto produto = compra.getProduto();
        StringBuilder corpoEmail = new StringBuilder();
        BigDecimal total = produto.getValor().multiply(new BigDecimal(compra.getQuantidade()));

        corpoEmail.append("Pagamento concluído com sucesso! Detalhes do pedido:");
        corpoEmail.append("\nID compra: ").append(compra.getId());
        corpoEmail.append("\nForma pagamento: ").append(compra.getFormaPagmento().toString());
        corpoEmail.append("\nProduto: ").append(produto.getNome());
        corpoEmail.append("\nQuantidade: ").append(compra.getQuantidade());
        corpoEmail.append("\nTotal: ").append(total);
        corpoEmail.append("\nVendedor: ").append(compra.getEmailVendedor());

        TemplateEmail templateEmail = new TemplateEmail(
                corpoEmail.toString(),
                "Pagamento concluído...",
                "Sistema mercado livre",
                "sistema@nossomercadolivre.com",
                compra.getEmailCliente());
        mailer.send(templateEmail);
    }

}