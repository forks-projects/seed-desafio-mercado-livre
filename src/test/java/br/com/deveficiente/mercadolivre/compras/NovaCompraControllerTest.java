package br.com.deveficiente.mercadolivre.compras;

import br.com.deveficiente.mercadolivre.compartilhado.seguranca.AutorizacaoHelper;
import br.com.deveficiente.mercadolivre.compartilhado.seguranca.TokenManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.jqwik.api.ForAll;
import net.jqwik.api.Label;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.spring.JqwikSpringSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;

import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@JqwikSpringSupport
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)

class NovaCompraControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private ObjectMapper objectMapper;

    @Property(tries = 10)
    @Label("Cadastro de compra com sucesso")
    void cadastroCompraComSucesso(
            @ForAll @IntRange(min = 1, max = 1000) int quantidade,
            @ForAll FormaPagmento formaPagamento
    ) throws Exception {
        int idProduto = 4;
        Map<String, Object> payload = Map.of(
                "idProduto", idProduto,
                "quantidade", quantidade,
                "formaPagamento", formaPagamento
        );

        String regexUrlRedirecionamento = String.format("http://gatewaypagamento\\.%s\\.com/\\d+\\?redirectUrl=http://localhost/v1/compras/\\d+",
                formaPagamento.toString().toLowerCase());
        mvc.perform(MockMvcRequestBuilders.post("/v1/compras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(new AutorizacaoHelper().getAuthorization(tokenManager))
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isFound())
                .andExpect(header().string(HttpHeaders.LOCATION, matchesPattern(regexUrlRedirecionamento)));
    }

    @Test
    @DisplayName("Cadastro de compra com produto inexistente")
    void cadastroCompraProdutoInexistente() throws Exception {
        Map<String, Object> payload = Map.of(
                "idProduto", 999,
                "quantidade", 1,
                "formaPagamento", FormaPagmento.PAGSEGURO
        );

        mvc.perform(MockMvcRequestBuilders.post("/v1/compras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(new AutorizacaoHelper().getAuthorization(tokenManager))
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.listaErros[0].campo").value("idProduto"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("não encontrado(a)"))
        ;
    }

    @Test
    @DisplayName("Cadastro de compra sem autenticação")
    void cadastroCompraSemAutenticacao() throws Exception {
        Map<String, Object> payload = Map.of(
                "idProduto", 1,
                "quantidade", 1,
                "formaPagamento", FormaPagmento.PAYPAL
        );

        mvc.perform(MockMvcRequestBuilders.post("/v1/compras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Cadastro de compra com quantidade inválida")
    void cadastroCompraQuantidadeInvalida() throws Exception {
        Map<String, Object> payload = Map.of(
                "idProduto", 1,
                "quantidade", 0,
                "formaPagamento", FormaPagmento.PAGSEGURO
        );

        mvc.perform(MockMvcRequestBuilders.post("/v1/compras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(new AutorizacaoHelper().getAuthorization(tokenManager))
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Cadastro de compra com forma de pagamento inválida")
    void cadastroCompraFormaPagamentoInvalida() throws Exception {
        Map<String, Object> payload = Map.of(
                "idProduto", 1,
                "quantidade", 1,
                "formaPagamento", "FORMA_PAGAMENTO_INVALIDA"
        );

        mvc.perform(MockMvcRequestBuilders.post("/v1/compras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(new AutorizacaoHelper().getAuthorization(tokenManager))
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());
    }

}