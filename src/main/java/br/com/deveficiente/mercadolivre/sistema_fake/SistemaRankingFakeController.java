package br.com.deveficiente.mercadolivre.sistema_fake;

import br.com.deveficiente.mercadolivre.compras.RankingRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rankings")
public class SistemaRankingFakeController {

    @PostMapping
    public ResponseEntity<Object> registrar(@Valid @RequestBody RankingRequest request) {
        System.out.println("========================================");
        System.out.println("Enviando RANKING para sistema externo");
        System.out.println(String.format("ID da compra: %s", request.idCompra()));
        System.out.println(String.format("ID do vendedor: %s", request.idVendedor()));
        System.out.println("========================================");
        return ResponseEntity.ok().build();
    }
}
