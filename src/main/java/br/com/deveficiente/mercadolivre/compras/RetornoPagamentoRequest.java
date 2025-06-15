package br.com.deveficiente.mercadolivre.compras;

import br.com.deveficiente.mercadolivre.compartilhado.validacao.ExisteId;
import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Objects;

public record RetornoPagamentoRequest(
    @NotNull
    @ExisteId(classeDaEntidade = Compra.class, nomeDoCampo = "id")
    Long idCompra,

    @NotBlank
    String idPagamento,

    @NotBlank
    String statusPagamento
) {
    public Pagamento toModel(@NotNull EntityManager entityManager) {
        Map<String, String> valoresValidos = Map.of(
                "SUCESSO", "SUCESSO",
                "ERRO", "ERRO",
                "1", "SUCESSO",
                "0", "ERRO"
        );

        String status = valoresValidos.get(statusPagamento.toUpperCase().trim());
        if (Objects.isNull(status)) {
            throw new IllegalArgumentException("Status inválido. Valores válidos: " + valoresValidos.keySet());
        }
        Compra compra = entityManager.find(Compra.class, idCompra);
        Assert.notNull(compra, "Compra não pode ser nulo");
        return new Pagamento(idPagamento, StatusPagamento.valueOf(status), compra);
    }
}
