package br.com.deveficiente.mercadolivre.compras;

import br.com.deveficiente.mercadolivre.produtos.Produto;
import br.com.deveficiente.mercadolivre.usuarios.Usuario;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "compras")
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private Usuario usuario;

    @NotNull
    @ManyToOne
    private Produto produto;

    @Positive
    private Integer quantidade;

    @NotNull
    private FormaPagmento formaPagmento;

    @NotNull
    private StatusCompra status;

    /**
     * Para uso do ORM
     */
    @Deprecated
    public Compra() {
    }

    // dica/anotações para quem usar o construtor saber os valores obrigatórios
    public Compra(@NotNull Usuario usuario,
                  @NotNull Produto produto,
                  @Positive Integer quantidade,
                  @NotNull String formaPagamento) {
        this.usuario = usuario;
        this.produto = produto;
        this.quantidade = quantidade;
        this.status = StatusCompra.INICIADA;
        this.formaPagmento = FormaPagmento.valueOf(formaPagamento);
    }

    public Long getId() {
        return id;
    }

    public FormaPagmento getFormaPagmento() {
        return formaPagmento;
    }

    public Long getIdVendedor() {
        return this.produto.getIdVendedor();
    }

    public String getEmailVendedor() {
        return this.produto.getEmailVendedor();
    }

    public Long getIdCliente() {
        return this.usuario.getId();
    }

    public String getEmailCliente() {
        return this.usuario.getLogin();
    }

}
