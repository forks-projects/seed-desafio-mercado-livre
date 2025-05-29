package br.com.deveficiente.mercadolivre.produtos;

import br.com.deveficiente.mercadolivre.categorias.Categoria;
import br.com.deveficiente.mercadolivre.produtos.caracteristicas.Caracteristica;
import br.com.deveficiente.mercadolivre.produtos.imagens.ImagemProduto;
import br.com.deveficiente.mercadolivre.produtos.opnioes.OpiniaoProduto;
import br.com.deveficiente.mercadolivre.produtos.perguntas.PerguntaProduto;
import br.com.deveficiente.mercadolivre.usuarios.Usuario;
import io.jsonwebtoken.lang.Assert;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "produtos", uniqueConstraints = {
        @UniqueConstraint(name = "uc_produtos_nome", columnNames = "nome")
})
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @Positive
    @NotNull
    private BigDecimal valor;

    @NotNull
    @Min(0)
    private Integer quantidade;

    @NotBlank
    @Size(max = 1000, min = 1)
    private String descricao;

    @ManyToOne
    @NotNull
    private Categoria categoria;

    @ManyToOne
    @NotNull
    private  Usuario usuario;

    @Size(min = 3, message = "deve ter pelo menos 3 características")
    @NotNull
    @OneToMany(mappedBy = "produto", cascade = CascadeType.PERSIST)
    private Set<Caracteristica> caracteristicas = new HashSet<>();

    @OneToMany(mappedBy = "produto", cascade = CascadeType.MERGE)
    private Set<ImagemProduto> imagens = new HashSet<>();

    @OneToMany(mappedBy = "produto", cascade = CascadeType.MERGE)
    private Set<OpiniaoProduto> opinioes = new HashSet<>();

    @OneToMany(mappedBy = "produto", cascade = CascadeType.MERGE)
    private List<PerguntaProduto> perguntas = new ArrayList<>();

    private LocalDateTime dataHoraRegistro;

    /**
     * Somente para uso do ORM
     */
    @Deprecated
    public Produto() {}

    // Informação natural e obrigatória entra pelo construtor
    public Produto(String nome,
                   BigDecimal valor,
                   Integer quantidade,
                   String descricao,
                   Categoria categoria,
                   Usuario usuario,
                   Set<Caracteristica> caracteristicas) {

        this.nome = nome;
        this.valor = valor;
        this.quantidade = quantidade;
        this.descricao = descricao;
        this.categoria = categoria;
        this.usuario = usuario;
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

    // dica/anotações para quem usar o construtor saber os valores obrigatórios
    public void adicionarImagens(@NotNull @Size(min = 1) Set<ImagemProduto> imagens) {
        //self testing/ design by contrato
        Assert.notNull(imagens, "imagens não pode ser nulo");
        Assert.isTrue(!imagens.isEmpty(), "deve ter pelo menos 1 imagem");
        imagens.forEach(imagem -> imagem.associarProduto(this));
        this.imagens.addAll(imagens);
    }

    // dica/anotações para quem usar o construtor saber os valores obrigatórios
    public void adicionarPergunta(@NotNull PerguntaProduto pergunta) {
        //self testing/ design by contrato
        Assert.notNull(pergunta, "Pergunta não pode ser nula");
        this.perguntas.add(pergunta);
    }

    public boolean pertenceA(Usuario usuario) {
        return this.usuario.equals(usuario);
    }

    public String getEmailVendedor() {
        return this.usuario.getLogin();
    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public Set<Caracteristica> getCaracteristicas() {
        return caracteristicas;
    }

    public String getDescricao() {
        return descricao;
    }

    public Set<OpiniaoProduto> getOpinioes() {
        return opinioes;
    }

    public List<PerguntaProduto> getPerguntas() {
        return perguntas;
    }

    public Set<ImagemProduto> getImagens() {
        return imagens;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public double getNotaMedia() {
        double media = this.getOpinioes().stream()
                .mapToInt(OpiniaoProduto::getNota)
                .average()
                .orElse(0.0);
        return Math.round(media * 100.0) / 100.0;
    }

    // dica/anotações para quem usar o construtor saber os valores obrigatórios
    public void abaterEstoque(@Positive Integer quantidadeAbate) {
        //self testing/ design by contrato
        Assert.notNull(quantidadeAbate, "Quantidade de abate não pode ser nula");
        Assert.isTrue(quantidadeAbate > 0, "Quantidade de abate deve ser maior que zero");
        Assert.isTrue(this.quantidade >= quantidadeAbate, "Quantidade do produto insuficiente. Existe somente " + this.quantidade + " unidade(s) no estoque");
        this.quantidade -= quantidadeAbate;
    }
}
