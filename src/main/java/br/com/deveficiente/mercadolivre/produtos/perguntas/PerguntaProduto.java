package br.com.deveficiente.mercadolivre.produtos.perguntas;

import br.com.deveficiente.mercadolivre.produtos.Produto;
import br.com.deveficiente.mercadolivre.usuarios.Usuario;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "perguntas_produto")
public class PerguntaProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String titulo;

    @NotNull
    @ManyToOne
    private Produto produto;

    @NotNull
    @ManyToOne
    private Usuario usuario;

    @NotNull
    private LocalDateTime dataHoraRegistro;

    /**
     * Somente para uso do ORM
     */
    @Deprecated
    public PerguntaProduto() {}

    public PerguntaProduto(@NotBlank String titulo,
                           @NotNull Produto produto,
                           @NotNull Usuario usuario) {
        this.titulo = titulo;
        this.produto = produto;
        this.usuario = usuario;
        this.dataHoraRegistro = LocalDateTime.now();
    }

}
