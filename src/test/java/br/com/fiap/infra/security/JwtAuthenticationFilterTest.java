package br.com.fiap.infra.security;

import br.com.fiap.infra.services.JwtTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtAuthenticationFilter - Testes Unitários")
class JwtAuthenticationFilterTest {

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        SecurityContextHolder.clearContext();

        userDetails = User.builder()
                .username("joao")
                .password("senha123")
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_CLIENTE")))
                .build();
    }

    @Test
    @DisplayName("Deve autenticar usuário com token JWT válido")
    void deveAutenticarUsuarioComTokenValido() throws ServletException, IOException {
        String token = "valid.jwt.token";
        request.addHeader("Authorization", "Bearer " + token);

        when(jwtTokenService.extractUsername(token)).thenReturn("joao");
        when(userDetailsService.loadUserByUsername("joao")).thenReturn(userDetails);
        when(jwtTokenService.validateToken(token, userDetails)).thenReturn(true);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).isEqualTo(userDetails);
        assertThat(SecurityContextHolder.getContext().getAuthentication().getAuthorities())
                .extracting("authority")
                .contains("ROLE_CLIENTE");

        verify(jwtTokenService).extractUsername(token);
        verify(userDetailsService).loadUserByUsername("joao");
        verify(jwtTokenService).validateToken(token, userDetails);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Deve continuar sem autenticação quando não há header Authorization")
    void deveContinuarSemAutenticacaoQuandoNaoHaHeader() throws ServletException, IOException {
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(jwtTokenService, never()).extractUsername(anyString());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Deve continuar sem autenticação quando header não começa com Bearer")
    void deveContinuarSemAutenticacaoQuandoHeaderInvalido() throws ServletException, IOException {
        request.addHeader("Authorization", "Basic invalid-token");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(jwtTokenService, never()).extractUsername(anyString());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Deve continuar sem autenticação quando token é inválido")
    void deveContinuarSemAutenticacaoQuandoTokenInvalido() throws ServletException, IOException {
        String token = "invalid.jwt.token";
        request.addHeader("Authorization", "Bearer " + token);

        when(jwtTokenService.extractUsername(token)).thenReturn("joao");
        when(userDetailsService.loadUserByUsername("joao")).thenReturn(userDetails);
        when(jwtTokenService.validateToken(token, userDetails)).thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Deve tratar SignatureException e continuar sem autenticação")
    void deveTratarSignatureException() throws ServletException, IOException {
        String token = "token.with.invalid.signature";
        request.addHeader("Authorization", "Bearer " + token);

        when(jwtTokenService.extractUsername(token)).thenThrow(new SignatureException("Invalid signature"));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Deve tratar ExpiredJwtException e continuar sem autenticação")
    void deveTratarExpiredJwtException() throws ServletException, IOException {
        String token = "expired.jwt.token";
        request.addHeader("Authorization", "Bearer " + token);

        when(jwtTokenService.extractUsername(token))
                .thenThrow(new ExpiredJwtException(null, null, "Token expired"));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Deve tratar MalformedJwtException e continuar sem autenticação")
    void deveTratarMalformedJwtException() throws ServletException, IOException {
        String token = "malformed.jwt.token";
        request.addHeader("Authorization", "Bearer " + token);

        when(jwtTokenService.extractUsername(token)).thenThrow(new MalformedJwtException("Malformed token"));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Deve tratar Exception genérica e continuar sem autenticação")
    void deveTratarExceptionGenerica() throws ServletException, IOException {
        String token = "problematic.token";
        request.addHeader("Authorization", "Bearer " + token);

        when(jwtTokenService.extractUsername(token)).thenThrow(new RuntimeException("Unexpected error"));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Não deve autenticar quando já existe autenticação no contexto")
    void naoDeveAutenticarQuandoJaExisteAutenticacao() throws ServletException, IOException {
        String token = "valid.jwt.token";
        request.addHeader("Authorization", "Bearer " + token);

        // Simula que já existe uma autenticação no contexto
        SecurityContextHolder.getContext().setAuthentication(
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()));

        when(jwtTokenService.extractUsername(token)).thenReturn("joao");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(jwtTokenService, never()).validateToken(anyString(), any());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Deve extrair token corretamente do header Bearer")
    void deveExtrairTokenCorretamenteDoHeader() throws ServletException, IOException {
        String token = "my.jwt.token";
        request.addHeader("Authorization", "Bearer " + token);

        when(jwtTokenService.extractUsername(token)).thenReturn("maria");
        when(userDetailsService.loadUserByUsername("maria")).thenReturn(userDetails);
        when(jwtTokenService.validateToken(token, userDetails)).thenReturn(true);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtTokenService).extractUsername(token);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Deve continuar a cadeia de filtros independente do resultado")
    void deveContinuarCadeiaFiltrosIndependenteResultado() throws ServletException, IOException {
        // Caso 1: Sem token
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);

        // Caso 2: Com token válido
        request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer valid.token");
        when(jwtTokenService.extractUsername("valid.token")).thenReturn("user");
        when(userDetailsService.loadUserByUsername("user")).thenReturn(userDetails);
        when(jwtTokenService.validateToken("valid.token", userDetails)).thenReturn(true);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain, times(2)).doFilter(any(), any());
    }
}