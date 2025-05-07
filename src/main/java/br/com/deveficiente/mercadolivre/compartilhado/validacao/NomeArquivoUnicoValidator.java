package br.com.deveficiente.mercadolivre.compartilhado.validacao;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

//1 ICP: NomeArquivoUnico
public class NomeArquivoUnicoValidator implements ConstraintValidator<NomeArquivoUnico, List<MultipartFile>> {
    @Override
    public boolean isValid(List<MultipartFile> files, ConstraintValidatorContext context) {
        //1 ICP: if
        if (files == null || files.isEmpty()) return true;

        Set<String> fileNames = new HashSet<>();
        //1 ICP: for
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            //1 ICP: if
            if (!fileNames.add(fileName)) {
                return false;
            }
        }
        return true;
    }
}
