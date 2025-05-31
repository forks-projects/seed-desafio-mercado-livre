package br.com.deveficiente.mercadolivre.compartilhado.service;

public interface Mailer {

    /**
     * @param templateEmail
     */
    void send(TemplateEmail templateEmail);

}