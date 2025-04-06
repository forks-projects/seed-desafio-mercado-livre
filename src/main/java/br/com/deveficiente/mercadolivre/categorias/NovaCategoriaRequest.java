package br.com.deveficiente.mercadolivre.categorias;

import br.com.deveficiente.mercadolivre.compartilhado.validacao.ExisteId;
import br.com.deveficiente.mercadolivre.compartilhado.validacao.ValorUnico;
import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.NotBlank;
import org.springframework.util.Assert;

import java.util.Objects;

public record NovaCategoriaRequest(
    @NotBlank
    @ValorUnico(classeDaEntidade = Categoria.class, nomeDoCampo = "nome")
    String nome,

    @ExisteId(nomeDoCampo = "id", classeDaEntidade = Categoria.class)
    Long idCategoriaMae
) {
    public Categoria toModel(EntityManager entityManager) {
        //self testing/ design by contrato
        Assert.hasLength(nome, "Nome não pode estar em branco");
        Categoria categoria = new Categoria(nome);
        if(Objects.isNull(idCategoriaMae)) return categoria;

        Categoria categoriaMae = entityManager.find(Categoria.class, idCategoriaMae);
        //self testing/ design by contrato
        Assert.notNull(categoriaMae, "Categoria mãe não encontrada");
        categoria.adicionaCategoriaMae(categoriaMae);
        return categoria;
    }
}
