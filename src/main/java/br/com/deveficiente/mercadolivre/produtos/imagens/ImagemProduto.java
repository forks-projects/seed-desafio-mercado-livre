package br.com.deveficiente.mercadolivre.produtos.imagens;

import br.com.deveficiente.mercadolivre.produtos.Produto;
import io.jsonwebtoken.lang.Assert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

@Entity
@Table(name = "imagens_produto")
public class ImagemProduto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    private String urlImagem;

    @ManyToOne
    private Produto produto;

    /**
     * Somente para uso do ORM
     */
    @Deprecated
    public ImagemProduto() {}

    // Informação natural e obrigatória entra pelo construtor
    // dica/anotações para quem usar o construtor saber os valores obrigatórios
    public ImagemProduto(@NotBlank String nome, @NotBlank String urlImagem) {
        this.nome = nome;
        this.urlImagem = urlImagem;
    }

    public void associarProduto(@NotNull Produto produto) {
        Assert.notNull(produto, "Produto não pode ser nulo");
        this.produto = produto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImagemProduto that)) return false;
        return Objects.equals(nome, that.nome) && Objects.equals(urlImagem, that.urlImagem) && Objects.equals(produto, that.produto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, urlImagem, produto);
    }
}
