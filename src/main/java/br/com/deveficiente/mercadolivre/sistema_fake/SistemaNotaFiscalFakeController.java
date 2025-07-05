package br.com.deveficiente.mercadolivre.sistema_fake;

import br.com.deveficiente.mercadolivre.compras.NotaFiscalRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notas-fiscais")
public class SistemaNotaFiscalFakeController {

    @PostMapping
    public ResponseEntity<Object> registrar(@Valid @RequestBody NotaFiscalRequest request) {
        System.out.println("========================================");
        System.out.println("Enviando nota fiscal para sistema externo");
        System.out.println(String.format("ID da compra: %s", request.idCompra()));
        System.out.println(String.format("ID do cliente: %s", request.idCliente()));
        System.out.println("========================================");
        return ResponseEntity.ok().build();
    }
}
