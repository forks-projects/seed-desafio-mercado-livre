package br.com.deveficiente.mercadolivre.compras;

import br.com.deveficiente.mercadolivre.categorias.Categoria;
import br.com.deveficiente.mercadolivre.produtos.Produto;
import br.com.deveficiente.mercadolivre.produtos.caracteristicas.Caracteristica;
import br.com.deveficiente.mercadolivre.usuarios.SenhaLimpa;
import br.com.deveficiente.mercadolivre.usuarios.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class RetornoPagamentoRequestTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;

    private Usuario criaUsuario() {
        return new Usuario("teste@email.com", new SenhaLimpa("senha123"));
    }

    private Categoria criaCategoria() {
        return new Categoria("Eletrônicos");
    }

    private Produto criaProduto() {
        Usuario dono = new Usuario("vendedor@email.com", new SenhaLimpa("123456"));
        Set<Caracteristica> caracteristicas = Set.of(
                new Caracteristica("Tela", "6 polegadas"),
                new Caracteristica("Memória", "128GB"),
                new Caracteristica("Câmera", "12MP")
        );
        return new Produto("Celular",
                BigDecimal.valueOf(1200),
                50,
                "Smartphone novo",
                criaCategoria(),
                dono,
                caracteristicas);
    }

    private Compra criaCompra() {
        Usuario usuario = criaUsuario();
        Produto produto = criaProduto();
        return new Compra(usuario, produto, 2, "PAYPAL");
    }

    @DisplayName("Deve criar Pagamento com status")
    @ParameterizedTest
    @ValueSource(strings = {
            "SUCESSO", " SUCESSO ", "SUCessO","sucesso", " sucesso ","1", " 1 ",
            "ERRO", " ERRO ", "ERro", "0", " 0 "
    })
    void deveCriarPagamentoComStatus(String statusPagamento) {
        Long idCompra = 1L;
        String idPagamento = UUID.randomUUID().toString();
        RetornoPagamentoRequest request = new RetornoPagamentoRequest(idCompra, idPagamento, statusPagamento);
        Compra compra = criaCompra();
        when(entityManager.find(Compra.class, idCompra)).thenReturn(compra);

        String sqlPagamentoRealizado = "select p from Pagamento p where p.idPagamento = :pIdPagamento AND p.statusPagamento = 1";
        when(entityManager.createQuery(contains(sqlPagamentoRealizado))).thenReturn(query);

        String sqlCompraRegistrada = "select p from Pagamento p where p.compra.id = :pIdCompra";
        when(entityManager.createQuery(contains(sqlCompraRegistrada))).thenReturn(query);

        when(query.setParameter("pIdPagamento", idPagamento)).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.emptyList());

        Pagamento pagamento = request.toModel(entityManager);

        Map<String, String> valoresValidos = Map.of(
                "ERRO", "ERRO",
                "0", "ERRO",
                "SUCESSO", "SUCESSO",
                "1", "SUCESSO"
        );
        String statusValido = valoresValidos.get(statusPagamento.toUpperCase().trim());
        assertNotNull(pagamento);
        assertEquals(idPagamento, pagamento.getIdPagamento());
        assertEquals(statusValido, pagamento.getStatusPagamento().toString());
        assertEquals(compra, pagamento.getCompra());
        verify(entityManager, times(1)).find(Compra.class, idCompra);
        verify(entityManager, times(1)).createQuery(sqlPagamentoRealizado);
        verify(entityManager, times(1)).createQuery(sqlCompraRegistrada);
        verify(query, times(2)).getResultList();
        assertEquals(pagamento.getIdPagamento(), idPagamento);
    }

    @Test
    @DisplayName("Deve lançar exceção para status de pagamento inválido")
    void deveLancarExcecaoParaStatusDePagamentoInvalido() {
        Long idCompra = 1L;
        String idPagamento = UUID.randomUUID().toString();
        String statusPagamento = "STATUS_INEXISTENTE";
        RetornoPagamentoRequest request = new RetornoPagamentoRequest(idCompra, idPagamento, statusPagamento);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> request.toModel(entityManager));

        String mensagemErro = "Status de pagamento inválido. Exemplos de valores válidos: [";
        assertTrue(ex.getMessage().startsWith(mensagemErro));
        verify(entityManager, never()).find(eq(Compra.class), anyLong());
        verify(entityManager, never()).createQuery(anyString());
    }

    @Test
    @DisplayName("Deve lançar exceção quando criar Pagamento com compra não cadastrada")
    void deveLancarExcecaoQuandoCriarPagamentoComCompraNaoCadastrada() {
        Long idCompra = 999L;
        String idPagamento = "id_pagamento_inexistente";
        String statusPagamento = "SUCESSO";
        RetornoPagamentoRequest request = new RetornoPagamentoRequest(idCompra, idPagamento, statusPagamento);
        when(entityManager.find(Compra.class, idCompra)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> request.toModel(entityManager));

        assertEquals("Compra não encontrada", ex.getMessage());
        verify(entityManager, times(1)).find(Compra.class, idCompra);
        verify(entityManager, never()).createQuery(anyString());
    }

    @Test
    @DisplayName("Deve lançar exceção quando criar Pagamento que já foi realizado com Status sucesso")
    void deveLancarExcecaoQuandoCriarPagamentoQueJaFoiRealizadoComStatusSucesso() {
        Long idCompra = 1L;
        String idPagamentoComStatusSucessoRegistrado = UUID.randomUUID().toString();
        String statusPagamento = "SUCESSO";
        RetornoPagamentoRequest request = new RetornoPagamentoRequest(idCompra, idPagamentoComStatusSucessoRegistrado, statusPagamento);
        Compra compra = criaCompra();
        Pagamento pagamentoExistente = new Pagamento(idPagamentoComStatusSucessoRegistrado, StatusPagamento.SUCESSO, compra);
        String sqlPagamentoRealizado = "select p from Pagamento p where p.idPagamento = :pIdPagamento AND p.statusPagamento = 1";

        when(entityManager.find(Compra.class, idCompra)).thenReturn(compra);
        when(entityManager.createQuery(sqlPagamentoRealizado)).thenReturn(query);
        when(query.setParameter("pIdPagamento", idPagamentoComStatusSucessoRegistrado)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(pagamentoExistente));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> request.toModel(entityManager));

        assertEquals("Pagamento já foi realizado", ex.getMessage());
        verify(entityManager, times(1)).find(Compra.class, idCompra);
        verify(entityManager, times(1)).createQuery(sqlPagamentoRealizado);
        verify(query, times(1)).setParameter("pIdPagamento", idPagamentoComStatusSucessoRegistrado);
        verify(query, times(1)).getResultList();
    }

    @Test
    @DisplayName("Deve lançar exceção quando criar Pagamento com compra associada a outro pagamento")
    void deveLancarExcecaoQuandoCriarPagamentoComCompraAssociadaAOutroPagamento() {
        Long idCompra = 1L;
        String idPagamentoNovo = UUID.randomUUID().toString();
        String idPagamentoAntigo = UUID.randomUUID().toString();
        String statusPagamento = "SUCESSO";
        RetornoPagamentoRequest request = new RetornoPagamentoRequest(idCompra, idPagamentoNovo, statusPagamento);
        Compra compra = criaCompra();
        Pagamento pagamentoAntigo = new Pagamento(idPagamentoAntigo, StatusPagamento.SUCESSO, compra);
        String sqlPagamentoRealizado = "select p from Pagamento p where p.idPagamento = :pIdPagamento AND p.statusPagamento = 1";
        String sqlCompraRegistrada = "select p from Pagamento p where p.compra.id = :pIdCompra";

        when(entityManager.find(Compra.class, idCompra)).thenReturn(compra);
        when(entityManager.createQuery(sqlPagamentoRealizado)).thenReturn(query);
        when(query.setParameter("pIdPagamento", idPagamentoNovo)).thenReturn(query);

        when(entityManager.createQuery(sqlCompraRegistrada)).thenReturn(query);
        when(query.setParameter("pIdCompra", idCompra)).thenReturn(query);
        when(query.getResultList())
                .thenReturn(Collections.emptyList())
                .thenReturn(List.of(pagamentoAntigo));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> request.toModel(entityManager));

        String mensagemErro = "Esta compra está associada a outro pagamento. Id da compra: " + idCompra;
        assertEquals(mensagemErro, ex.getMessage());
        verify(entityManager, times(1)).find(Compra.class, idCompra);
        verify(entityManager, times(1)).createQuery(sqlPagamentoRealizado);
        verify(entityManager, times(1)).createQuery(sqlCompraRegistrada);
        verify(query, times(2)).getResultList();
        verify(query, times(1)).setParameter("pIdPagamento", idPagamentoNovo);
        verify(query, times(1)).setParameter("pIdCompra", idCompra);
    }

    @Test
    @DisplayName("Deve criar pagamento com status sucesso quando pagamento já está cadastrado com status de erro")
    void deveCriarPagamentoComStatusSucessoQuandoPagamentoJaEstaCadastradoComStatusErro() {
        Long idCompra = 1L;
        String idPagamento = "id_pagamento_paypal_erro";
        String statusPagamentoNovo = "SUCESSO";
        RetornoPagamentoRequest request = new RetornoPagamentoRequest(idCompra, idPagamento, statusPagamentoNovo);
        Compra compra = criaCompra();
        Pagamento pagamentoAnteriorErro = new Pagamento(idPagamento, StatusPagamento.ERRO, compra);

        when(entityManager.find(Compra.class, idCompra)).thenReturn(compra);

        String sqlPagamentoRealizado = "select p from Pagamento p where p.idPagamento = :pIdPagamento AND p.statusPagamento = 1";
        when(entityManager.createQuery(sqlPagamentoRealizado)).thenReturn(query);
        when(query.setParameter("pIdPagamento", idPagamento)).thenReturn(query);
        when(query.getResultList())
                .thenReturn(Collections.emptyList())
                .thenReturn(List.of(pagamentoAnteriorErro));

        String sqlCompraRegistrada = "select p from Pagamento p where p.compra.id = :pIdCompra";
        when(entityManager.createQuery(sqlCompraRegistrada)).thenReturn(query);
        when(query.setParameter("pIdCompra", idCompra)).thenReturn(query);

        Pagamento pagamento = request.toModel(entityManager);

        assertNotNull(pagamento);
        assertEquals(idPagamento, pagamento.getIdPagamento());
        assertEquals(StatusPagamento.SUCESSO, pagamento.getStatusPagamento());
        assertEquals(compra, pagamento.getCompra());
        verify(entityManager, times(1)).find(Compra.class, idCompra);
        verify(entityManager, times(1)).createQuery(sqlPagamentoRealizado);
        verify(entityManager, times(1)).createQuery(sqlCompraRegistrada);
        verify(query, times(2)).getResultList();
    }

}