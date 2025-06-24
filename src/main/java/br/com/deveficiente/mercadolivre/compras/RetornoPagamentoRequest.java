package br.com.deveficiente.mercadolivre.compras;

import br.com.deveficiente.mercadolivre.compartilhado.validacao.ExisteId;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public record RetornoPagamentoRequest(
    @NotNull
    @ExisteId(classeDaEntidade = Compra.class, nomeDoCampo = "id")
    // 1 ICP: Compra
    Long idCompra,

    @NotBlank
    String idPagamento,

    @NotBlank
    String statusPagamento
) {
    // dica (anotações) para quem usar o construtor
    public Pagamento toModel(@NotNull EntityManager entityManager) {
        Map<String, String> valoresValidos = Map.of(
                "ERRO", "ERRO",
                "0", "ERRO",
                "SUCESSO", "SUCESSO",
                "1", "SUCESSO"
        );
        String statusValido = valoresValidos.get(statusPagamento.toUpperCase().trim());
        // 1 ICP assert
        Assert.notNull(statusValido, "Status de pagamento inválido. Exemplos de valores válidos: " + valoresValidos.keySet());

        Compra compra = entityManager.find(Compra.class, idCompra);
        // 1 ICP assert
        Assert.notNull(compra, "Compra não encontrada");

        String sqlPagamentoRealizado = "select p from Pagamento p where p.idPagamento = :pIdPagamento AND p.statusPagamento = 1";
        Query queryPagamentoRealizado = entityManager.createQuery(sqlPagamentoRealizado);
        queryPagamentoRealizado.setParameter("pIdPagamento", idPagamento);
        //1 ICP: if
        if (!queryPagamentoRealizado.getResultList().isEmpty()) {
            throw new IllegalArgumentException("Pagamento já foi realizado");
        }

        String sqlCompraRegistrada = "select p from Pagamento p where p.compra.id = :pIdCompra";
        Query queryCompraRegistrada = entityManager.createQuery(sqlCompraRegistrada);
        queryCompraRegistrada.setParameter("pIdCompra", idCompra);
        List resultList = queryCompraRegistrada.getResultList();
        //1 ICP: if
        if (!resultList.isEmpty()) {
            //1 ICP: Pagamento
            Pagamento pagamento = (Pagamento) resultList.get(0);
            //1 ICP: if
            if (!Objects.equals(pagamento.getIdPagamento(), idPagamento)) {
                throw new IllegalArgumentException("Esta compra está associada a outro pagamento" + idCompra);
            }
        }
        //1 ICP: StatusPagamento
        return new Pagamento(idPagamento, StatusPagamento.valueOf(statusValido), compra);
    }
}
