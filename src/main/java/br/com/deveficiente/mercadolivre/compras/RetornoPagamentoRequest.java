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
    // 1 ICP: Pagamento
    public Pagamento toModel(@NotNull EntityManager entityManager) {
        String statusPagamentoValido = getStatusPagamentoValido(statusPagamento);
        Compra compra = entityManager.find(Compra.class, idCompra);
        //1 ICP: if
        Assert.notNull(compra, "Compra não encontrada");

        validarRetornoPagamento(entityManager);

        //1 ICP: StatusPagamento
        return new Pagamento(idPagamento, StatusPagamento.valueOf(statusPagamentoValido), compra);
    }

    private void validarRetornoPagamento(EntityManager entityManager) {
        verificarPagamentoRealizado(entityManager);
        verificarCompraAssociadaAOutroPagamento(entityManager);
    }

    /**
     * Mapeia o status de pagamento de entrada para um valor padronizado (SUCESSO/ERRO).
     *
     * @param statusPagamento O status de pagamento recebido na requisição.
     * @return O status de pagamento padronizado.
     * @throws IllegalArgumentException Se o status de pagamento for inválido.
     */
    private String getStatusPagamentoValido(String statusPagamento) {
        Map<String, String> valoresValidos = Map.of(
                "ERRO", "ERRO",
                "0", "ERRO",
                "SUCESSO", "SUCESSO",
                "1", "SUCESSO"
        );
        String statusValido = valoresValidos.get(statusPagamento.toUpperCase().trim());
        // 1 ICP assert
        Assert.notNull(statusValido, "Status de pagamento inválido. Exemplos de valores válidos: " + valoresValidos.keySet());
        return statusValido;
    }

    /**
     * Verifica se o pagamento já foi realizado
     *
     * @param entityManager O EntityManager para interagir com o banco de dados.
     * @throws IllegalArgumentException Se o pagamento já tiver sido realizado.
     */
    private void verificarPagamentoRealizado(@NotNull EntityManager entityManager) {
        String sqlPagamentoRealizado = "select p from Pagamento p where p.idPagamento = :pIdPagamento AND p.statusPagamento = 1";
        Query queryPagamentoRealizado = entityManager.createQuery(sqlPagamentoRealizado);
        queryPagamentoRealizado.setParameter("pIdPagamento", idPagamento);
        //1 ICP: if
        if (!queryPagamentoRealizado.getResultList().isEmpty()) {
            throw new IllegalArgumentException("Pagamento já foi realizado");
        }
    }

    /**
     * Verifica se a compra está associada a outro pagamento.
     *
     * @param entityManager O EntityManager para interagir com o banco de dados.
     * @throws IllegalArgumentException Se a compra estiver associada a outro pagamento.
     */
    private void verificarCompraAssociadaAOutroPagamento(@NotNull EntityManager entityManager) {
        String sqlCompraRegistrada = "select p from Pagamento p where p.compra.id = :pIdCompra";
        Query queryCompraRegistrada = entityManager.createQuery(sqlCompraRegistrada);
        queryCompraRegistrada.setParameter("pIdCompra",
                idCompra);
        List resultList = queryCompraRegistrada.getResultList();

        //1 ICP: if
        if (!resultList.isEmpty()) {
            Pagamento pagamentoExistente = (Pagamento) resultList.get(0);
            //1 ICP: if
            if (!Objects.equals(pagamentoExistente.getIdPagamento(), idPagamento)) {
                throw new IllegalArgumentException("Esta compra está associada a outro pagamento. Id da compra: " + idCompra);
            }
        }
    }
}
