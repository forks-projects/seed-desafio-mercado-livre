package br.com.deveficiente.mercadolivre.compras;

import br.com.deveficiente.mercadolivre.compartilhado.seguranca.AutorizacaoHelper;
import br.com.deveficiente.mercadolivre.compartilhado.seguranca.TokenManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ForAll;
import net.jqwik.api.Label;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.spring.JqwikSpringSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@JqwikSpringSupport
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class RetornoPagamentoControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RestTemplate restTemplate;

    @Value("${sistema.externo.notas-fiscais}")
    private String urlSistemaExternoNotasFiscais;

    @Value("${sistema.externo.rankings}")
    private String urlSistemaExternoRankings;

    @Property(tries = 10)
    @Label("Cadastro de retorno de compra com sucesso")
    void cadastroRetornoCompraComSucesso(
            @ForAll @IntRange(min = 1, max = 1000) int quantidade,
            @ForAll FormaPagmento formaPagamento,
            @ForAll("statusValues") String statusPagamento
    ) throws Exception {
        int idProduto = 4;
        Map<String, Object> payload = Map.of(
                "idProduto", idProduto,
                "quantidade", quantidade,
                "formaPagamento", formaPagamento
        );

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/v1/compras")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(new AutorizacaoHelper().getAuthorization(tokenManager))
                .content(objectMapper.writeValueAsString(payload)))
                .andReturn();
        String headerUrlRedirecionamento = mvcResult.getResponse().getHeader(HttpHeaders.LOCATION);
        String idCompra = extrairIdUrl(headerUrlRedirecionamento);

        UUID uuid = UUID.randomUUID();
        Map<String, Object> payloadRetorno = Map.of(
                "idPagamento", uuid.toString(),
                "idCompra", idCompra,
                "statusPagamento", statusPagamento
        );

        mvc.perform(MockMvcRequestBuilders.post("/v1/retornos-pagamento")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(new AutorizacaoHelper().getAuthorization(tokenManager))
                .content(objectMapper.writeValueAsString(payloadRetorno)))
                .andExpect(status().isOk());

        verify(restTemplate, times(1)).postForEntity(eq(urlSistemaExternoNotasFiscais), any(NotaFiscalRequest.class), eq(Void.class));
        verify(restTemplate, times(1)).postForEntity(eq(urlSistemaExternoRankings), any(RankingRequest.class), eq(Void.class));
    }

    @Property(tries = 1)
    @Label("Cadastrar retorno compra com erro")
    void cadastrarRetornoCompraComErro(
            @ForAll @IntRange(min = 1, max = 1000) int quantidade,
            @ForAll FormaPagmento formaPagamento
    ) throws Exception {
        int idProduto = 4;
        Map<String, Object> payload = Map.of(
                "idProduto", idProduto,
                "quantidade", quantidade,
                "formaPagamento", formaPagamento
        );

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/v1/compras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(new AutorizacaoHelper().getAuthorization(tokenManager))
                        .content(objectMapper.writeValueAsString(payload)))
                .andReturn();
        String headerUrlRedirecionamento = mvcResult.getResponse().getHeader(HttpHeaders.LOCATION);
        String idCompra = extrairIdUrl(headerUrlRedirecionamento);

        UUID uuid = UUID.randomUUID();
        Map<String, Object> payloadRetorno = Map.of(
                "idPagamento", uuid.toString(),
                "idCompra", idCompra,
                "statusPagamento", "ERRO"
        );

        mvc.perform(MockMvcRequestBuilders.post("/v1/retornos-pagamento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(new AutorizacaoHelper().getAuthorization(tokenManager))
                        .content(objectMapper.writeValueAsString(payloadRetorno)))
                .andExpect(status().isOk());
    }

    @Property(tries = 10)
    @Label("Cadastrar retorno compra com status sucesso quando pagamento já está cadastrado com status de erro")
    void cadastrarRetornoCompraComStatusSucessoQuandoPagamentoJaEstaRegistradoComStatusErro(
            @ForAll @IntRange(min = 1, max = 1000) int quantidade,
            @ForAll FormaPagmento formaPagamento
    ) throws Exception {
        int idProduto = 4;
        Map<String, Object> payload = Map.of(
                "idProduto", idProduto,
                "quantidade", quantidade,
                "formaPagamento", formaPagamento
        );

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/v1/compras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(new AutorizacaoHelper().getAuthorization(tokenManager))
                        .content(objectMapper.writeValueAsString(payload)))
                .andReturn();
        String headerUrlRedirecionamento = mvcResult.getResponse().getHeader(HttpHeaders.LOCATION);
        String idCompra = extrairIdUrl(headerUrlRedirecionamento);

        UUID uuid = UUID.randomUUID();
        String idPagamentoErro = uuid.toString();
        Map<String, Object> payloadRetornoStatusErro = Map.of(
                "idPagamento", idPagamentoErro,
                "idCompra", idCompra,
                "statusPagamento", "ERRO"
        );

        mvc.perform(MockMvcRequestBuilders.post("/v1/retornos-pagamento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(new AutorizacaoHelper().getAuthorization(tokenManager))
                        .content(objectMapper.writeValueAsString(payloadRetornoStatusErro)));

        Map<String, Object> payloadRetornoStatusSucesso = Map.of(
                "idPagamento", idPagamentoErro,
                "idCompra", idCompra,
                "statusPagamento", "SUCESSO"
        );

        mvc.perform(MockMvcRequestBuilders.post("/v1/retornos-pagamento")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(new AutorizacaoHelper().getAuthorization(tokenManager))
                .content(objectMapper.writeValueAsString(payloadRetornoStatusSucesso)))
                .andExpect(status().isOk());

        verify(restTemplate, times(1)).postForEntity(eq(urlSistemaExternoNotasFiscais), any(NotaFiscalRequest.class), eq(Void.class));
        verify(restTemplate, times(1)).postForEntity(eq(urlSistemaExternoRankings), any(RankingRequest.class), eq(Void.class));
    }

    public static String extrairIdUrl(String url) {
        Pattern pattern = Pattern.compile("/(\\d+)$");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) return matcher.group(1);
        return null;
    }

    @Provide
    Arbitrary<String> statusValues() {
        return Arbitraries.of("SUCESSO", "1");
    }

}
