package br.com.deveficiente.mercadolivre.produtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public record ImagensRequest(
        @NotNull
        @Size(min = 1)
        Set<MultipartFile> imagens
) {}