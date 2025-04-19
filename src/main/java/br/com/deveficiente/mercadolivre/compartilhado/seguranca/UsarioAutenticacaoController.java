package br.com.deveficiente.mercadolivre.compartilhado.seguranca;

import br.com.deveficiente.mercadolivre.compartilhado.excecao.ResponseErroDTO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class UsarioAutenticacaoController {
    private AuthenticationManager authManager;

    private TokenManager tokenManager;

    private static final Logger log = LoggerFactory.getLogger(UsarioAutenticacaoController.class);

    public UsarioAutenticacaoController(AuthenticationManager authManager, TokenManager tokenManager) {
        this.authManager = authManager;
        this.tokenManager = tokenManager;
    }

    @PostMapping
    public ResponseEntity<Object> authenticate(@Valid @RequestBody LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = loginRequest.build();

        try {
            Authentication authentication = authManager.authenticate(authenticationToken);
            String jwt = tokenManager.generateToken(authentication);

            return ResponseEntity.ok(jwt);
        } catch (AuthenticationException exception) {
            log.error("[Autenticacao]", exception);
            ResponseErroDTO responseErroDTO = new ResponseErroDTO(400, exception.getMessage(), List.of());
            return ResponseEntity.badRequest().body(responseErroDTO);
        }

    }
}