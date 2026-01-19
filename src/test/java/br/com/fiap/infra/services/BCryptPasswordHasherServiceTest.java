package br.com.fiap.infra.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("BCryptPasswordHasherService - Testes Unitários")
class BCryptPasswordHasherServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    private BCryptPasswordHasherService service;

    @BeforeEach
    void setUp() {
        service = new BCryptPasswordHasherService(passwordEncoder);
    }

    @Test
    @DisplayName("Deve retornar true quando senha corresponde ao hash")
    void deveRetornarTrueQuandoSenhaCorrespondeAoHash() {
        String plainPassword = "senha123";
        String hash = "$2a$10$hashedpassword";
        when(passwordEncoder.matches(plainPassword, hash)).thenReturn(true);

        boolean result = service.matches(plainPassword, hash);

        assertThat(result).isTrue();
        verify(passwordEncoder).matches(plainPassword, hash);
    }

    @Test
    @DisplayName("Deve retornar false quando senha não corresponde ao hash")
    void deveRetornarFalseQuandoSenhaNaoCorrespondeAoHash() {
        String plainPassword = "senhaErrada";
        String hash = "$2a$10$hashedpassword";
        when(passwordEncoder.matches(plainPassword, hash)).thenReturn(false);

        boolean result = service.matches(plainPassword, hash);

        assertThat(result).isFalse();
        verify(passwordEncoder).matches(plainPassword, hash);
    }

    @Test
    @DisplayName("Deve codificar senha corretamente")
    void deveCodificarSenhaCorretamente() {
        String plainPassword = "senha123";
        String expectedHash = "$2a$10$newhashedpassword";
        when(passwordEncoder.encode(plainPassword)).thenReturn(expectedHash);

        String result = service.encode(plainPassword);

        assertThat(result).isEqualTo(expectedHash);
        verify(passwordEncoder).encode(plainPassword);
    }

    @Test
    @DisplayName("Deve delegar encoding para PasswordEncoder")
    void deveDelegarEncodingParaPasswordEncoder() {
        String password = "minhasenha";
        when(passwordEncoder.encode(anyString())).thenReturn("hash");

        service.encode(password);

        verify(passwordEncoder).encode(password);
    }
}