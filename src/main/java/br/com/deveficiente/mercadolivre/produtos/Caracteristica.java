package br.com.deveficiente.mercadolivre.produtos;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.util.Assert;

@Entity
@Table(name = "caracteristicas")
public class Caracteristica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    private String descricao;

    @ManyToOne
    private Produto produto;

    @Deprecated
    public Caracteristica() {}

    // dica/anotações para quem usar o construtor saber os valores obrigatórios
    public Caracteristica(@NotBlank String nome, @NotBlank String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    // dica/anotações para quem usar o construtor saber os valores obrigatórios
    public void vinculaAProduto(@NotNull Produto produto) {
        Assert.notNull(produto, "produto não pode ser nulo");
        this.produto = produto;
    }

}
