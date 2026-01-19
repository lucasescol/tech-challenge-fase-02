package br.com.fiap.infra.gateways;

import br.com.fiap.core.domain.Endereco;
import br.com.fiap.core.domain.TipoUsuario;
import br.com.fiap.core.domain.Usuario;
import br.com.fiap.core.exceptions.EmailJaCadastradoException;
import br.com.fiap.core.exceptions.LoginJaCadastradoException;
import br.com.fiap.core.exceptions.UsuarioNaoEncontradoException;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JpaUsuarioGateway - Testes Unitários")
class JpaUsuarioGatewayTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private JpaUsuarioGateway gateway;

    private Usuario usuarioDomain;
    private UsuarioEntity usuarioEntity;

    @BeforeEach
    void setUp() {
        TipoUsuario tipo = TipoUsuario.create(1L, "CLIENTE", "Cliente");
        Endereco endereco = new Endereco("Rua A", "100", "", "Bairro", "Cidade", "SP", "00000-000");
        usuarioDomain = Usuario.create(1L, "João", "joao@email.com", "joao", "senha123", endereco, tipo);

        TipoUsuarioEntity tipoEntity = new TipoUsuarioEntity(1L, "CLIENTE", "Cliente");
        EnderecoEntity enderecoEntity = new EnderecoEntity(1L, "Rua A", "100", "", "Bairro", "Cidade", "SP",
                "00000000");
        usuarioEntity = new UsuarioEntity(1L, "João", "joao@email.com", "joao", "encodedPass", enderecoEntity,
                tipoEntity, null, null);
    }

    @Test
    @DisplayName("Deve incluir usuário com sucesso")
    void deveIncluirUsuario() {
        when(usuarioRepository.findByLogin("joao")).thenReturn(Optional.empty());
        when(usuarioRepository.findByEmail("joao@email.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("senha123")).thenReturn("encodedPass");
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuarioEntity);

        Usuario resultado = gateway.incluir(usuarioDomain);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        verify(usuarioRepository).save(any(UsuarioEntity.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao incluir usuário com login existente")
    void deveLancarExcecaoLoginExistenteAoIncluir() {
        when(usuarioRepository.findByLogin("joao")).thenReturn(Optional.of(usuarioEntity));

        assertThatThrownBy(() -> gateway.incluir(usuarioDomain))
                .isInstanceOf(LoginJaCadastradoException.class);

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao incluir usuário com email existente")
    void deveLancarExcecaoEmailExistenteAoIncluir() {
        when(usuarioRepository.findByLogin("joao")).thenReturn(Optional.empty());
        when(usuarioRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(usuarioEntity));

        assertThatThrownBy(() -> gateway.incluir(usuarioDomain))
                .isInstanceOf(EmailJaCadastradoException.class);

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve atualizar usuário com sucesso")
    void deveAtualizarUsuario() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        when(usuarioRepository.findByLogin("joao")).thenReturn(Optional.of(usuarioEntity)); // Mesmo ID
        when(usuarioRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(usuarioEntity)); // Mesmo ID
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuarioEntity);

        Usuario resultado = gateway.atualizar(1L, usuarioDomain);

        assertThat(resultado).isNotNull();
        verify(usuarioRepository).save(any(UsuarioEntity.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar usuário inexistente")
    void deveLancarExcecaoUsuarioInexistenteAoAtualizar() {
        when(usuarioRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> gateway.atualizar(1L, usuarioDomain))
                .isInstanceOf(UsuarioNaoEncontradoException.class);
    }

    @Test
    @DisplayName("Deve excluir usuário com sucesso")
    void deveExcluirUsuario() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(1L);

        gateway.excluir(1L);

        verify(usuarioRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao excluir usuário inexistente")
    void deveLancarExcecaoUsuarioInexistenteAoExcluir() {
        when(usuarioRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> gateway.excluir(1L))
                .isInstanceOf(UsuarioNaoEncontradoException.class);
    }

    @Test
    @DisplayName("Deve trocar senha com sucesso")
    void deveTrocarSenha() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity));
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuarioEntity);

        gateway.trocarSenha(1L, "novaSenha");

        verify(usuarioRepository).save(any(UsuarioEntity.class));
        assertThat(usuarioEntity.getSenha()).isEqualTo("novaSenha");
    }

    @Test
    @DisplayName("Deve buscar todos os usuários")
    void deveBuscarTodos() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuarioEntity));

        List<Usuario> resultado = gateway.buscarTodos();

        assertThat(resultado).hasSize(1);
    }

    @Test
    @DisplayName("Deve buscar por nome")
    void deveBuscarPorNome() {
        when(usuarioRepository.findByNomeContainingIgnoreCase("João")).thenReturn(List.of(usuarioEntity));

        List<Usuario> resultado = gateway.buscarPorNome("João");

        assertThat(resultado).hasSize(1);
    }

    @Test
    @DisplayName("Deve buscar por ID")
    void deveBuscarPorId() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity));

        Optional<Usuario> resultado = gateway.buscarPorId(1L);

        assertThat(resultado).isPresent();
    }

    @Test
    @DisplayName("Deve buscar por Login")
    void deveBuscarPorLogin() {
        when(usuarioRepository.findByLogin("joao")).thenReturn(Optional.of(usuarioEntity));

        Optional<Usuario> resultado = gateway.buscarPorLogin("joao");

        assertThat(resultado).isPresent();
    }
}