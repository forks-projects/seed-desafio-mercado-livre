package br.com.deveficiente.mercadolivre.usuarios;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

// Informação natural e obrigatória entra pelo construtor
// dica/anotações para quem usar o construtor saber os valores obrigatórios
public record NovoUsuarioRequest(
    @NotBlank
    @Email
    String login,

    @NotBlank
    @Length(min = 6, max = 15)
    String senha
) {
    public Usuario toModel() {
        return new Usuario(login, new SenhaLimpa(senha));
    }
}
