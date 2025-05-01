package br.com.deveficiente.mercadolivre.compartilhado.seguranca;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public class AutorizacaoHelper {
    public HttpHeaders getAuthorization(TokenManager tokenManager) {
        UserDetails user = org.springframework.security.core.userdetails.User
                .withUsername("adriano@email.com")
                .password("123456")
                .build();

        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        String jwtToken = tokenManager.generateToken(auth);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        return headers;
    }
}