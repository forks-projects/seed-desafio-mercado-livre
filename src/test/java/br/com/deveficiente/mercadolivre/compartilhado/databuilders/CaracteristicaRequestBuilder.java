package br.com.deveficiente.mercadolivre.compartilhado.databuilders;

import br.com.deveficiente.mercadolivre.produtos.caracteristicas.CaracteristicaRequest;

public class CaracteristicaRequestBuilder {

    private String nome = "cor padrão";
    private String descricao = "descrição padrão";

    private CaracteristicaRequestBuilder() {
    }

    public static CaracteristicaRequestBuilder umaCaracteristica() {
        return new CaracteristicaRequestBuilder();
    }

    public CaracteristicaRequestBuilder comNome(String nome) {
        this.nome = nome;
        return this;
    }

    public CaracteristicaRequestBuilder comDescricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public CaracteristicaRequest build() {
        return new CaracteristicaRequest(nome, descricao);
    }
}
