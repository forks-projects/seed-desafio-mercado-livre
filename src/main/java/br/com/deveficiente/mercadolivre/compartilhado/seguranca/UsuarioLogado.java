package br.com.deveficiente.mercadolivre.compartilhado.seguranca;

import br.com.deveficiente.mercadolivre.usuarios.Usuario;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UsuarioLogado implements UserDetails {
    private Usuario usuario;
    private User springUserDetails;

    public UsuarioLogado(@NotNull @Valid Usuario usuario) {
        this.usuario = usuario;
        springUserDetails = new User(usuario.getLogin(), usuario.getSenha(), List.of());
    }

    public Collection<GrantedAuthority> getAuthorities() {
        return springUserDetails.getAuthorities();
    }

    public String getPassword() {
        return springUserDetails.getPassword();
    }

    public String getUsername() {
        return springUserDetails.getUsername();
    }

    @Override
    public boolean isEnabled() {
        return springUserDetails.isEnabled();
    }

    @Override
    public boolean isAccountNonExpired() {
        return springUserDetails.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return springUserDetails.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return springUserDetails.isCredentialsNonExpired();
    }

}