package br.com.deveficiente.mercadolivre.compras;

import br.com.deveficiente.mercadolivre.compartilhado.validacao.ExisteId;
import br.com.deveficiente.mercadolivre.usuarios.Usuario;
import jakarta.validation.constraints.NotNull;

public record NotaFiscalRequest(
        @NotNull
        @ExisteId(classeDaEntidade = Compra.class, nomeDoCampo = "id")
        Long idCompra,

        @NotNull
        @ExisteId(classeDaEntidade = Usuario.class, nomeDoCampo = "id")
        Long idCliente
) {
}
