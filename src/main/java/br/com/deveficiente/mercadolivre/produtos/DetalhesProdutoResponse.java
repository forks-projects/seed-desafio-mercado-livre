package br.com.deveficiente.mercadolivre.produtos;

import br.com.deveficiente.mercadolivre.produtos.caracteristicas.CaracteristicaResponse;
import br.com.deveficiente.mercadolivre.produtos.imagens.ImagemProduto;
import br.com.deveficiente.mercadolivre.produtos.opnioes.OpiniaoProdutoResponse;
import br.com.deveficiente.mercadolivre.produtos.perguntas.PerguntaProdutoResponse;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record DetalhesProdutoResponse(
        String nome,
        BigDecimal preco,
        String descricao,
        double notaMedia,
        Integer numeroTotalNotas,
        Set<CaracteristicaResponse> caractesristicas,
        List<String> imagens,
        Set<OpiniaoProdutoResponse> opinioes,
        Set<PerguntaProdutoResponse> perguntas
) {
    // dica/anotações para quem usar o construtor saber os valores obrigatórios
    public DetalhesProdutoResponse(@NotNull Produto produto) {
        this(
                produto.getNome(),
                produto.getValor(),
                produto.getDescricao(),
                produto.getNotaMedia(),
                produto.getOpinioes().size(),
                produto.getCaracteristicas().stream().map(CaracteristicaResponse::new).collect(Collectors.toSet()),
                produto.getImagens().stream().map(ImagemProduto::getUrlImagem).toList(),
                produto.getOpinioes().stream().map(OpiniaoProdutoResponse::new).collect(Collectors.toSet()),
                produto.getPerguntas().stream().map(PerguntaProdutoResponse::new).collect(Collectors.toSet())
        );
    }
}
