package br.com.deveficiente.mercadolivre.produtos;

import java.util.Set;

public interface Uploader {
    Set<ImagemProduto> enviar(ImagensRequest imagens);
}
