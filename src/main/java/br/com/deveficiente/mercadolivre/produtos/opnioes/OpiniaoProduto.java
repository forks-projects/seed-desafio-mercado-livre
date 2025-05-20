package br.com.deveficiente.mercadolivre.produtos.opnioes;

import br.com.deveficiente.mercadolivre.produtos.Produto;
import br.com.deveficiente.mercadolivre.usuarios.Usuario;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "opinioes_produto")
public class OpiniaoProduto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(1)
    @Max(5)
    @NotNull
    private Integer nota;

    @NotBlank
    private String titulo;

    @NotBlank
    @Length(min = 3, max = 500)
    private String descricao;

    @NotNull
    @ManyToOne
    private Produto produto;

    @NotNull
    @ManyToOne
    private Usuario usuario;

    /**
     * Somente para uso do ORM
     */
    @Deprecated
    public OpiniaoProduto() {}

    // Informação natural e obrigatória entra pelo construtor
    // dica/anotações para quem usar o construtor saber os valores obrigatórios
    public OpiniaoProduto(@Min(1) @Max(5) @NotNull Integer nota,
                          @NotBlank String titulo,
                          @NotBlank @Length(min = 3, max = 500) String descricao,
                          @NotNull Produto produto,
                          @NotNull Usuario usuario) {
        this.nota = nota;
        this.titulo = titulo;
        this.descricao = descricao;
        this.produto = produto;
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public Integer getNota() {
        return nota;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getEmailCliente() {
        return this.usuario.getLogin();
    }

}
