package br.com.deveficiente.mercadolivre.compartilhado.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
public class FakerEmail implements Mailer {
    @Override
    public void send(TemplateEmail templateEmail) {
        System.out.println("=======================");
        System.out.println("Envia email FAKE");
        System.out.println(templateEmail.corpoEmail());
        System.out.println(templateEmail.assunto());
        System.out.println(templateEmail.nomeVisualizacao());
        System.out.println(templateEmail.emailOrigem());
        System.out.println(templateEmail.emailDestino());
        System.out.println("=======================");
    }
}
