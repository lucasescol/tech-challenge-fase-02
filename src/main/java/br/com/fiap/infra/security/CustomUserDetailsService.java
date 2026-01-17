package br.com.fiap.infra.security;

import br.com.fiap.infra.persistence.jpa.entities.UsuarioEntity;
import br.com.fiap.infra.persistence.jpa.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsuarioEntity usuario = usuarioRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

        return User.builder()
                .username(usuario.getLogin())
                .password(usuario.getSenha())
                .authorities(Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_" + usuario.getTipoUsuario().getNome().toUpperCase())
                ))
                .build();
    }
}
