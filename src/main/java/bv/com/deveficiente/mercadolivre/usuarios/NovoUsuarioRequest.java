package bv.com.deveficiente.mercadolivre.usuarios;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record NovoUsuarioRequest(
    @NotBlank
    @Email
    String login,

    @NotBlank
    @Length(min = 6, max = 15)
    String senha
) {
    public Usuario toModel() {
        return new Usuario(login, senha);
    }
}
