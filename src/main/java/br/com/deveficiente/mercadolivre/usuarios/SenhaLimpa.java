package br.com.deveficiente.mercadolivre.usuarios;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Assert;

/**
 * Representa uma senha limpa. Sem criptografia
 */
public class SenhaLimpa {
    private String senha;

    public SenhaLimpa(@NotBlank @Length(min = 6, max = 15) String senha) {
        //self testing/ design by contrato
        Assert.hasLength(senha, "senha não pode estar em branco");
        Assert.isTrue(senha.length()>=6,"senha tem que ter no mínimo 6 caracteres");
        this.senha = senha;
    }

    public String hash() {
        return new BCryptPasswordEncoder().encode(senha);
    }
}
