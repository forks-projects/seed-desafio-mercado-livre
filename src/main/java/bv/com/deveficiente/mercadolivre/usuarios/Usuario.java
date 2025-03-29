package bv.com.deveficiente.mercadolivre.usuarios;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Email
    private String login;

    @NotBlank
    @Length(min = 6, max = 15)
    private String senha;

    @NotNull
    @PastOrPresent
    private LocalDateTime dataHoraRegistro;

    /**
     * Somente para uso do ORM
     */
    @Deprecated
    public Usuario() {
    }

    // Informação natural e obrigatória entra pelo construtor
    // dica (anotações) para quem usar o construtor
    public Usuario(
            @NotBlank @Email String login,
            @NotBlank @Length(min = 6, max = 15) String senha
    ) {
        this.login = login;
        this.senha = senha;
        this.dataHoraRegistro = LocalDateTime.now();
    }
}
