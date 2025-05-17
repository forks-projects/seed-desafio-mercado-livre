package br.com.deveficiente.mercadolivre.produtos.perguntas;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public interface Mailer {

    /**
     *
     * @param corpoEmail     corpo do email
     * @param assunto  assunto do email
     * @param nomeVisualizacao nome para aparecer no provedor de email
     * @param emailOrigem     email de origem
     * @param emailDestino       email de destino
     */
    void send(@NotBlank String corpoEmail,
              @NotBlank String assunto,
              @NotBlank String nomeVisualizacao,
              @NotBlank @Email String emailOrigem,
              @NotBlank @Email String emailDestino);

}