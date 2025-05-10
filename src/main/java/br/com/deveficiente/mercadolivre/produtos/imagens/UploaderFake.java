package br.com.deveficiente.mercadolivre.produtos.imagens;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@Profile("dev")
public class UploaderFake implements Uploader{
    @Override
    // dica/anotações para quem usar o construtor saber os valores obrigatórios
    public Set<ImagemProduto> enviar(@NotNull @Size(min = 1) ImagensRequest imagens) {
        //self testing/ design by contrato
        Assert.notNull(imagens, "Request de imagens não pode ser nulo");
        Assert.isTrue(!imagens.imagens().isEmpty(), "Deve ter pelo menos uma imagem");
        // envia arquivo
        System.out.println("=============================================");
        System.out.println("Profile de DEV");
        System.out.println("=============================================");
        return imagens.imagens().stream().map(imagem -> {
            String nome = imagem.getOriginalFilename();
            String url = "//bucketname/" + imagem.getName();
            return new ImagemProduto(nome, url);
        }).collect(Collectors.toSet());
    }
}
