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

    public Pagamento() {
    }

    public Pagamento(@NotBlank @NotBlank String idPagamento,
                     @NotNull StatusPagamento statusPagamento,
                     @NotNull Compra compra) {
        this.idPagamento = idPagamento;
        this.statusPagamento = statusPagamento;
        this.compra = compra;
        this.dataHoraRegistro = LocalDateTime.now();
    }
}
