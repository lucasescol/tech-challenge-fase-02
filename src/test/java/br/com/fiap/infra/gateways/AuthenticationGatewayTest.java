package br.com.fiap.infra.gateways;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthenticationGateway - Testes Unitários")
class AuthenticationGatewayTest {

    @InjectMocks
    private AuthenticationGateway gateway;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Deve retornar usuário logado")
    void deveRetornarUsuarioLogado() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("joao");

        String usuario = gateway.getUsuarioLogado();

        assertThat(usuario).isEqualTo("joao");
    }

    @Test
    @DisplayName("Deve retornar null quando não há usuário logado")
    void deveRetornarNullQuandoNaoHaUsuarioLogado() {
        when(securityContext.getAuthentication()).thenReturn(null);

        String usuario = gateway.getUsuarioLogado();

        assertThat(usuario).isNull();
    }

    @Test
    @DisplayName("Deve verificar se é administrador")
    void deveVerificarSeEhAdministrador() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))).when(authentication).getAuthorities();

        boolean isAdmin = gateway.isAdministrador();

        assertThat(isAdmin).isTrue();
    }

    @Test
    @DisplayName("Deve retornar tipo de usuário logado")
    void deveRetornarTipoUsuarioLogado() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_CLIENTE"))).when(authentication).getAuthorities();

        String tipo = gateway.getTipoUsuarioLogado();

        assertThat(tipo).isEqualTo("CLIENTE");
    }

    @Test
    @DisplayName("Deve verificar se é dono de restaurante")
    void deveVerificarSeEhDonoRestaurante() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_DONO_RESTAURANTE"))).when(authentication).getAuthorities();

        boolean isDono = gateway.isDonoRestaurante();

        assertThat(isDono).isTrue();
    }
}