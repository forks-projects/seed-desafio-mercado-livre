package br.com.deveficiente.mercadolivre.categorias;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import org.springframework.util.Assert;

@Entity
@Table(name = "categorias", uniqueConstraints = {
        @UniqueConstraint(name = "uc_categorias_nome", columnNames = "nome")
})
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @Nullable
    @ManyToOne
    private Categoria categoriaMae;

    /**
     * Somente para uso do ORM
     */
    @Deprecated
    public Categoria() {
    }

    public Categoria(@NotBlank String nome) {
        this.nome = nome;
    }

    public void adicionaCategoriaMae(Categoria categoriaMae) {
        Assert.notNull(categoriaMae, "Categoria mãe não pode ser nula");
        this.categoriaMae = categoriaMae;
    }

    public String getNome() {
        return nome;
    }

    public Categoria getCategoriaMae() {
        return this.categoriaMae;
    }
}
