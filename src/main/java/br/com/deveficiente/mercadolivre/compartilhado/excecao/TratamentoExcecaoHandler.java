package br.com.deveficiente.mercadolivre.compartilhado.excecao;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

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
    //1 ICP: ResponseErroDTO
    public ResponseErroDTO tratarErrosValidacaoCampos(MethodArgumentNotValidException exception) {
        List<FieldError> listaErros = exception.getBindingResult().getFieldErrors();
        //1 ICP: ErroDTO
        ArrayList<ErroDTO> errosNosCampos = new ArrayList<>();
        //1 ICP: forEach
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

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ResponseErroDTO> tratarResponseStatusExceptionForbidden(ResponseStatusException exception) {
        //1 ICP: if
        if (exception.getStatusCode() == HttpStatus.FORBIDDEN) {
            ResponseErroDTO responseErroDTO = new ResponseErroDTO(
                    HttpStatus.FORBIDDEN.value(),
                    "Acesso negado",
                    null
            );
            return new ResponseEntity<>(responseErroDTO, HttpStatus.FORBIDDEN);
        }

        //1 ICP: if
        if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
            ResponseErroDTO responseErroDTO = new ResponseErroDTO(
                    HttpStatus.NOT_FOUND.value(),
                    "Não encontrado",
                    null
            );
            return new ResponseEntity<>(responseErroDTO, HttpStatus.NOT_FOUND);
        }

        ResponseErroDTO responseErroDTO = new ResponseErroDTO(
        HttpStatus.BAD_REQUEST.value(),
        "Informação inválida",
        null
        );
        return new ResponseEntity<>(responseErroDTO, HttpStatus.BAD_REQUEST);
    }

}
