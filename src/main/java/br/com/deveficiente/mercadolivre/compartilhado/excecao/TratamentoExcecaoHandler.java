package br.com.deveficiente.mercadolivre.compartilhado.excecao;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class TratamentoExcecaoHandler {

    private final MessageSource messageSource;

    public TratamentoExcecaoHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseErroDTO tratarErrosValidacaoCampos(MethodArgumentNotValidException exception) {
        List<FieldError> listaErros = exception.getBindingResult().getFieldErrors();
        ArrayList<ErroDTO> errosNosCampos = new ArrayList<>();
        listaErros.forEach(error -> {
            String mensagem = messageSource.getMessage(error, LocaleContextHolder.getLocale());
            ErroDTO erro = new ErroDTO(error.getField(), mensagem);
            errosNosCampos.add(erro);
        });

        return new ResponseErroDTO(
                exception.getStatusCode().value(),
                "Informação inválida",
                errosNosCampos
        );
    }

}
