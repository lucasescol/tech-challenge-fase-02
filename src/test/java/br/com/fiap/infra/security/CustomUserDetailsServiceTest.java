package br.com.fiap.infra.security;

import br.com.fiap.infra.persistence.jpa.entities.EnderecoEntity;
import br.com.fiap.infra.persistence.jpa.entities.TipoUsuarioEntity;
import br.com.fiap.infra.persistence.jpa.entities.UsuarioEntity;
import br.com.fiap.infra.persistence.jpa.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CustomUserDetailsService - Testes Unitários")
class CustomUserDetailsServiceTest {

        @Mock
        private UsuarioRepository usuarioRepository;

        @InjectMocks
        private CustomUserDetailsService userDetailsService;

        private UsuarioEntity usuarioEntity;

        @BeforeEach
        void setUp() {
                EnderecoEntity endereco = new EnderecoEntity(
                                1L, "Rua A", "100", "", "Centro", "São Paulo", "SP", "01000-000");

                TipoUsuarioEntity tipoUsuario = new TipoUsuarioEntity(
                                1L, "CLIENTE", "Cliente");

                usuarioEntity = new UsuarioEntity(
                                1L,
                                "João Silva",
                                "joao@email.com",
                                "joao",
                                "$2a$10$encodedPassword",
                                endereco,
                                tipoUsuario,
                                null,
                                null);
        }

        @Test
        @DisplayName("Deve carregar usuário por username com sucesso")
        void deveCarregarUsuarioPorUsernameComSucesso() {
                when(usuarioRepository.findByLogin("joao")).thenReturn(Optional.of(usuarioEntity));

                UserDetails userDetails = userDetailsService.loadUserByUsername("joao");

                assertThat(userDetails).isNotNull();
                assertThat(userDetails.getUsername()).isEqualTo("joao");
                assertThat(userDetails.getPassword()).isEqualTo("$2a$10$encodedPassword");
                assertThat(userDetails.getAuthorities()).hasSize(1);
                assertThat(userDetails.getAuthorities())
                                .extracting("authority")
                                .contains("ROLE_CLIENTE");

                verify(usuarioRepository).findByLogin("joao");
        }

        @Test
        @DisplayName("Deve carregar usuário DONO_RESTAURANTE corretamente")
        void deveCarregarUsuarioDonoRestaurante() {
                TipoUsuarioEntity tipoDono = new TipoUsuarioEntity(2L, "DONO_RESTAURANTE", "Dono");
                usuarioEntity = new UsuarioEntity(
                                2L, "Maria", "maria@email.com", "maria", "senha", null, tipoDono, null, null);

                when(usuarioRepository.findByLogin("maria")).thenReturn(Optional.of(usuarioEntity));

                UserDetails userDetails = userDetailsService.loadUserByUsername("maria");

                assertThat(userDetails.getAuthorities())
                                .extracting("authority")
                                .contains("ROLE_DONO_RESTAURANTE");
        }

        @Test
        @DisplayName("Deve carregar usuário ADMINISTRADOR corretamente")
        void deveCarregarUsuarioAdministrador() {
                TipoUsuarioEntity tipoAdmin = new TipoUsuarioEntity(3L, "ADMINISTRADOR", "Admin");
                usuarioEntity = new UsuarioEntity(
                                3L, "Admin", "admin@email.com", "admin", "senha", null, tipoAdmin, null, null);

                when(usuarioRepository.findByLogin("admin")).thenReturn(Optional.of(usuarioEntity));

                UserDetails userDetails = userDetailsService.loadUserByUsername("admin");

                assertThat(userDetails.getAuthorities())
                                .extracting("authority")
                                .contains("ROLE_ADMINISTRADOR");
        }

        @Test
        @DisplayName("Deve lançar exceção quando usuário não existe")
        void deveLancarExcecaoQuandoUsuarioNaoExiste() {
                when(usuarioRepository.findByLogin("usuario_inexistente"))
                                .thenReturn(Optional.empty());

                assertThatThrownBy(() -> userDetailsService.loadUserByUsername("usuario_inexistente"))
                                .isInstanceOf(UsernameNotFoundException.class)
                                .hasMessageContaining("Usuário não encontrado: usuario_inexistente");

                verify(usuarioRepository).findByLogin("usuario_inexistente");
        }

        @Test
        @DisplayName("Deve adicionar prefixo ROLE_ na authority")
        void deveAdicionarPrefixoRoleNaAuthority() {
                when(usuarioRepository.findByLogin("joao")).thenReturn(Optional.of(usuarioEntity));

                UserDetails userDetails = userDetailsService.loadUserByUsername("joao");

                assertThat(userDetails.getAuthorities()).hasSize(1);
                assertThat(userDetails.getAuthorities().iterator().next().getAuthority())
                                .startsWith("ROLE_");
        }

        @Test
        @DisplayName("Deve converter tipo de usuário para uppercase na authority")
        void deveConverterTipoUsuarioParaUppercase() {
                TipoUsuarioEntity tipoMinusculo = new TipoUsuarioEntity(1L, "cliente", "Cliente");
                usuarioEntity = new UsuarioEntity(
                                4L, "Test", "test@email.com", "test", "senha", null, tipoMinusculo, null, null);

                when(usuarioRepository.findByLogin("test")).thenReturn(Optional.of(usuarioEntity));

                UserDetails userDetails = userDetailsService.loadUserByUsername("test");

                assertThat(userDetails.getAuthorities())
                                .extracting("authority")
                                .contains("ROLE_CLIENTE");
        }

        @Test
        @DisplayName("Deve retornar UserDetails com senha encodada")
        void deveRetornarUserDetailsComSenhaEncodada() {
                when(usuarioRepository.findByLogin("joao")).thenReturn(Optional.of(usuarioEntity));

                UserDetails userDetails = userDetailsService.loadUserByUsername("joao");

                assertThat(userDetails.getPassword()).isEqualTo("$2a$10$encodedPassword");
        }
}