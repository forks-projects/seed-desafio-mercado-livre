package br.com.deveficiente.mercadolivre.produtos;

import br.com.deveficiente.mercadolivre.categorias.Categoria;
import io.jsonwebtoken.lang.Assert;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    String nome;

    @Positive
    @NotNull
    BigDecimal valor;

    @NotNull
    @Min(0)
    Integer quantidade;

    @NotBlank
    @Size(max = 1000, min = 1)
    String descricao;

    @ManyToOne
    @NotNull
    private Categoria categoria;

    @Size(min = 3, message = "deve ter pelo menos 3 características")
    @NotNull
    @OneToMany(mappedBy = "produto", cascade = CascadeType.PERSIST)
    Set<Caracteristica> caracteristicas = new HashSet<>();

    private LocalDateTime dataHoraRegistro;

    @Deprecated
    public Produto() {}

    // Informação natural e obrigatória entra pelo construtor
    public Produto(String nome,
                   BigDecimal valor,
                   Integer quantidade,
                   String descricao,
                   Categoria categoria,
                   Set<Caracteristica> caracteristicas) {

        this.nome = nome;
        this.valor = valor;
        this.quantidade = quantidade;
        this.descricao = descricao;
        this.categoria = categoria;
        adicionarCaracteristicas(caracteristicas);
        dataHoraRegistro = LocalDateTime.now();
    }

    // dica/anotações para quem usar o construtor saber os valores obrigatórios
    private void adicionarCaracteristicas(@NotNull @Size(min = 3) Set<Caracteristica> caracteristicas) {
        //self testing/ design by contrato
        Assert.notNull(caracteristicas, "caracteristicas não pode ser nulo");
        Assert.isTrue(caracteristicas.size() >= 3, "deve ter pelo menos 3 características");
        caracteristicas.forEach(c -> c.vinculaAProduto(this));
        this.caracteristicas.addAll(caracteristicas);
    }

}
