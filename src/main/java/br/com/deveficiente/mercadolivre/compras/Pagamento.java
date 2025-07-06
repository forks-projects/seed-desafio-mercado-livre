package br.com.deveficiente.mercadolivre.compras;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
public class Pagamento {
    @Id
    private String idPagamento;

    @NotNull
    private StatusPagamento statusPagamento;

    private LocalDateTime dataHoraRegistro;

    @NotNull
    @OneToOne
    private Compra compra;

    /**
     * Somente para uso do ORM
     */
    @Deprecated
    public Pagamento() {
    }

    // Informação natural e obrigatória entra pelo construtor
    // dica (anotações) para quem usar o construtor
    public Pagamento(@NotBlank @NotBlank String idPagamento,
                     @NotNull StatusPagamento statusPagamento,
                     @NotNull Compra compra) {
        this.idPagamento = idPagamento;
        this.statusPagamento = statusPagamento;
        this.compra = compra;
        this.dataHoraRegistro = LocalDateTime.now();
    }

    public String getIdPagamento() {
        return idPagamento;
    }

    public StatusPagamento getStatusPagamento() {
        return statusPagamento;
    }

    public Compra getCompra() {
        return compra;
    }

    public void atualizaStatus(@NotNull StatusPagamento  statusPagamento) {
        this.statusPagamento = statusPagamento;
    }

    public Long getIdCliente() {
        return compra.getIdCliente();
    }

    public Long getIdVendedor() {
        return compra.getIdVendedor();
    }
}
