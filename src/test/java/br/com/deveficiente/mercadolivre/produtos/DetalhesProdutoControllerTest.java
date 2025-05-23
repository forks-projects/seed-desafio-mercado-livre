package br.com.deveficiente.mercadolivre.produtos;

import br.com.deveficiente.mercadolivre.compartilhado.seguranca.AutorizacaoHelper;
import br.com.deveficiente.mercadolivre.compartilhado.seguranca.TokenManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class DetalhesProdutoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TokenManager tokenManager;

    @Test
    @DisplayName("Detalhamento de produto sem opinião")
    void detalhamentoProdutoSemOpinioes() throws Exception {
        mvc.perform(get("/v1/produtos/1")
                        .headers(new AutorizacaoHelper().getAuthorization(tokenManager))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("celular"))
                .andExpect(jsonPath("$.preco").value(100.00))
                .andExpect(jsonPath("$.descricao").value("uma descricao"))
                .andExpect(jsonPath("$.notaMedia").value(0.00))
                .andExpect(jsonPath("$.numeroTotalNotas").value(0))
                .andExpect(jsonPath("$.caractesristicas").isNotEmpty())
                .andExpect(jsonPath("$.imagens").isEmpty())
                .andExpect(jsonPath("$.opinioes").isEmpty())
                .andExpect(jsonPath("$.perguntas").isEmpty());
    }

    @Test
    @DisplayName("Detalhamento de produto com opinioes")
    void detalhamentoProdutoComOpinioes() throws Exception {
        mvc.perform(get("/v1/produtos/3")
                        .headers(new AutorizacaoHelper().getAuthorization(tokenManager))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("celular 2"))
                .andExpect(jsonPath("$.preco").value(100.00))
                .andExpect(jsonPath("$.descricao").value("uma descricao"))
                .andExpect(jsonPath("$.notaMedia").value(4.00))
                .andExpect(jsonPath("$.numeroTotalNotas").value(2))
                .andExpect(jsonPath("$.caractesristicas").isNotEmpty())
                .andExpect(jsonPath("$.imagens").isNotEmpty())
                .andExpect(jsonPath("$.opinioes").isNotEmpty())
                .andExpect(jsonPath("$.perguntas").isNotEmpty());
    }

    @Test
    @DisplayName("Deve retornar todos os campos corretamente no detalhamento do produto")
    void deveRetornarDetalhamentoCompletoProduto() throws Exception {
        mvc.perform(get("/v1/produtos/3")
                        .headers(new AutorizacaoHelper().getAuthorization(tokenManager))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("celular 2"))
                .andExpect(jsonPath("$.preco").value(100.00))
                .andExpect(jsonPath("$.descricao").value("uma descricao"))
                .andExpect(jsonPath("$.notaMedia").value(4))
                .andExpect(jsonPath("$.numeroTotalNotas").value(2))

                // Características
                .andExpect(jsonPath("$.caractesristicas[0].id").value(9))
                .andExpect(jsonPath("$.caractesristicas[0].nome").value("marca"))
                .andExpect(jsonPath("$.caractesristicas[0].descricao").value("Motorola"))
                .andExpect(jsonPath("$.caractesristicas[1].id").value(7))
                .andExpect(jsonPath("$.caractesristicas[1].nome").value("peso"))
                .andExpect(jsonPath("$.caractesristicas[1].descricao").value("300g"))
                .andExpect(jsonPath("$.caractesristicas[2].id").value(8))
                .andExpect(jsonPath("$.caractesristicas[2].nome").value("cor"))
                .andExpect(jsonPath("$.caractesristicas[2].descricao").value("preto"))

                // Imagens
                .andExpect(jsonPath("$.imagens", containsInAnyOrder(
                        "//bucketname/_download.png",
                        "//bucketname/_sunflower.jpg"
                )))

                // Opiniões
                .andExpect(jsonPath("$.opinioes[0].id").value(1))
                .andExpect(jsonPath("$.opinioes[0].nota").value(5))
                .andExpect(jsonPath("$.opinioes[0].titulo").value("opniao 3141 Crona Glens"))
                .andExpect(jsonPath("$.opinioes[0].descricao").value("Voluntarius agnitio claustrum adnuo."))
                .andExpect(jsonPath("$.opinioes[0].emailCliente").value("adriano@email.com"))

                .andExpect(jsonPath("$.opinioes[1].id").value(2))
                .andExpect(jsonPath("$.opinioes[1].nota").value(3))
                .andExpect(jsonPath("$.opinioes[1].titulo").value("opniao 9956 Oceane Camp"))
                .andExpect(jsonPath("$.opinioes[1].descricao").value("Sit truculenter turba incidunt."))
                .andExpect(jsonPath("$.opinioes[1].emailCliente").value("maria@email.com"))

                // Perguntas
                .andExpect(jsonPath("$.perguntas[0].id").value(1))
                .andExpect(jsonPath("$.perguntas[0].titulo").value("pergunta appono turbo curriculum"))
                .andExpect(jsonPath("$.perguntas[0].emailCliente").value("maria@email.com"))
                .andExpect(jsonPath("$.perguntas[0].dataHoraRegistro").value("2025-05-18T14:38:22.529804"));
    }


    @Test
    @DisplayName("Detalhamento de produto inexistente")
    void detalhamentoProdutoNaoExiste() throws Exception {
        mvc.perform(get("/v1/produtos/9999")
                        .headers(new AutorizacaoHelper().getAuthorization(tokenManager))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro").value("Produto não encontrado"))
                .andExpect(jsonPath("$.status").value(404))
        ;
    }

    @Test
    @DisplayName("Busca detalhe de produto sem autenticação")
    void buscaDetalheDeProdutoSemAutenticacao() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/v1/produtos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
