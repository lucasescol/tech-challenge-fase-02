package br.com.fiap.infra.gateways;

import br.com.fiap.core.gateways.IAuthenticationGateway;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationGateway implements IAuthenticationGateway {
    
    @Override
    public String getUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        return authentication.getName();
    }
    
    @Override
    public String getTipoUsuarioLogado() {
        return getTipoUsuario();
    }
    
    @Override
    public boolean isAdministrador() {
        return hasRole("ADMIN");
    }
    
    @Override
    public boolean isDonoRestaurante() {
        return hasRole("DONO_RESTAURANTE");
    }
    
    @Override
    public boolean isCliente() {
        return hasRole("CLIENTE");
    }
    
    private boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        return authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(authority -> authority.equals("ROLE_" + role));
    }
    
    private String getTipoUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        return authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .findFirst()
            .map(authority -> authority.replace("ROLE_", ""))
            .orElse(null);
    }
}
