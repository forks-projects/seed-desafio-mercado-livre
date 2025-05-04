package br.com.deveficiente.mercadolivre.produtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;

public class ImagensRequest {
    @Size(min = 1)
    @NotNull
    private Set<MultipartFile> imagens = new HashSet<>();

    // Informação natural e obrigatória entra pelo construtor
    // dica/anotações para quem usar o construtor saber os valores obrigatórios
    public ImagensRequest(@NotNull @Size(min = 1) Set<MultipartFile> imagens) {
        this.imagens = imagens;
    }

    public Set<MultipartFile> getImagens() {
        return imagens;
    }

    public void setImagens(Set<MultipartFile> imagens) {
        this.imagens = imagens;
    }

}
