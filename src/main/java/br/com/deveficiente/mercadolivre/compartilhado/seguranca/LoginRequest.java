package br.com.deveficiente.mercadolivre.compartilhado.seguranca;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.Assert;

import java.util.regex.Pattern;

public class LoginRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UsernamePasswordAuthenticationToken build() {
        //self testing/ design by contrato
        Assert.hasText(this.email, "Email não pode estar em branco ou nulo");
        Pattern emailPattern = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
        Assert.isTrue(emailPattern.matcher(this.email).matches(), "Formato de email inválido");
        Assert.hasText(this.password, "Senha não pode estar em branco ou nula");

        return new UsernamePasswordAuthenticationToken(this.email, this.password);
    }
}