package br.com.deveficiente.mercadolivre.produtos;

import br.com.deveficiente.mercadolivre.compartilhado.validacao.NomeArquivoUnico;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ImagensRequest(
        @NotNull
        @Size(min = 1)
        @NomeArquivoUnico
        List<MultipartFile> imagens
) { }