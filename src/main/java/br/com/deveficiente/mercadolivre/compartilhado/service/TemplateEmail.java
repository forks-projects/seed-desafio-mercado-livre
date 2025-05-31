package br.com.deveficiente.mercadolivre.compartilhado.service;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record TemplateEmail(
        @NotBlank String corpoEmail,
        @NotBlank String assunto,
        @NotBlank String nomeVisualizacao,
        @NotBlank @Email String emailOrigem,
        @NotBlank @Email String emailDestino
) {
}