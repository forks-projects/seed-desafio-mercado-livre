package br.com.deveficiente.mercadolivre.produtos;

import br.com.deveficiente.mercadolivre.categorias.Categoria;
import br.com.deveficiente.mercadolivre.compartilhado.validacao.CaracteristicasComNomesUnicos;
import br.com.deveficiente.mercadolivre.compartilhado.validacao.ExisteId;
import br.com.deveficiente.mercadolivre.compartilhado.validacao.ValorUnico;
import br.com.deveficiente.mercadolivre.produtos.caracteristicas.Caracteristica;
import br.com.deveficiente.mercadolivre.produtos.caracteristicas.CaracteristicaRequest;
import br.com.deveficiente.mercadolivre.usuarios.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record NovoProdutoRequest(
    @NotBlank
    @ValorUnico(classeDaEntidade = Produto.class, nomeDoCampo = "nome")
    String nome,

    @Positive
    @NotNull
    BigDecimal valor,

    @NotNull
    @Min(0)
    Integer quantidade,

    @NotBlank
    @Size(max = 1000, min = 1)
    String descricao,

    @NotNull
    @ExisteId(classeDaEntidade = Categoria.class, nomeDoCampo = "id")
    Integer idCategoria,

    @Size(min = 3, message = "deve ter pelo menos 3 características")
    @NotNull
    @CaracteristicasComNomesUnicos
    @Valid
    List<CaracteristicaRequest> caracteristicasRequest
) {
    //1 ICP UsuarioLogado
    public Produto toModel(@NotNull EntityManager entityManager, @NotNull Usuario usuarioLogado) {
        // 1 ICP Categoria
        Categoria categoria = entityManager.find(Categoria.class, idCategoria());
        //self testing/ design by contrato
        Assert.notNull(categoria, "Categoria deve existir");
        Assert.notNull(usuarioLogado, "Usuario não pode ser nulo");
        Set<Caracteristica> caracteristicas = caracteristicasRequest().stream()
                .map(CaracteristicaRequest::toModel)
                .collect(Collectors.toSet());
        return new Produto(nome(), valor(),quantidade(), descricao(), categoria, usuarioLogado,caracteristicas);
    }
}
