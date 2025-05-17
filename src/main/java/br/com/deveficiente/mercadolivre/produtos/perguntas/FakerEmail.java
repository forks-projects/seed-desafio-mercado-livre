package br.com.deveficiente.mercadolivre.produtos.perguntas;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
public class FakerEmail implements Mailer {
    @Override
    public void send(String corpoEmail,
                     String assunto,
                     String nomeVisualizacao,
                     String emailOrigem,
                     String emailDestino) {
        System.out.println("=======================");
        System.out.println("Envia email FAKE");
        System.out.println(corpoEmail);
        System.out.println(assunto);
        System.out.println(nomeVisualizacao);
        System.out.println(emailOrigem);
        System.out.println(emailDestino);
        System.out.println("=======================");
    }
}
