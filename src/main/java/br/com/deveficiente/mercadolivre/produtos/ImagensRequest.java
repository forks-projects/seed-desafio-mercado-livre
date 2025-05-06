package br.com.deveficiente.mercadolivre.produtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;

public record ImagensRequest(
        @NotNull
        @Size(min = 1)
        List<MultipartFile> imagens
) {
    public ImagensRequest(@NotNull @Size(min = 1) List<MultipartFile> imagens) {
        HashSet<Object> imagensRepetidas = new HashSet<>();
        imagens.forEach(imagem -> imagensRepetidas.add(imagem.getOriginalFilename()));
        Assert.isTrue(imagensRepetidas.size() == imagens.size(), "Não pode existir imagens repetidas");
        this.imagens = imagens;
    }
}