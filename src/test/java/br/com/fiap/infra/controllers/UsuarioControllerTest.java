package br.com.fiap.infra.controllers;

import br.com.fiap.core.usecases.usuario.*;
import br.com.fiap.infra.dto.*;
import br.com.fiap.infra.services.JwtTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@DisplayName("UsuarioController - Testes Unitários")
class UsuarioControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private JwtTokenService jwtTokenService;

        @MockitoBean
        private UserDetailsService userDetailsService;

        @MockitoBean
        private CadastrarUsuarioUseCase cadastrarUsuarioUseCase;

        @MockitoBean
        private AtualizarUsuarioUseCase atualizarUsuarioUseCase;

        @MockitoBean
        private AssociarTipoDoUsuarioUseCase associarTipoDoUsuarioUseCase;

        @MockitoBean
        private ExcluirUsuarioUseCase excluirUsuarioUseCase;

        @MockitoBean
        private TrocarSenhaUseCase trocarSenhaUseCase;

        @MockitoBean
        private BuscarUsuariosPorNomeUseCase buscarUsuariosPorNomeUseCase;

        @MockitoBean
        private ListarTodosUsuariosUseCase listarTodosUsuariosUseCase;

        @Test
        @DisplayName("Deve cadastrar usuário sem autenticação")
        void deveCadastrarUsuarioSemAutenticacao() throws Exception {
                NovoUsuarioDTO dto = new NovoUsuarioDTO(
                                "João Silva",
                                "joao@email.com",
                                "joao",
                                "senha123",
                                "Rua A",
                                "100",
                                "",
                                "Centro",
                                "São Paulo",
                                "SP",
                                "01000-000",
                                "CLIENTE");

                CadastrarUsuarioUseCase.OutputModel output = new CadastrarUsuarioUseCase.OutputModel(
                                1L,
                                "João Silva",
                                "joao@email.com",
                                "joao",
                                "Rua A, 100, Centro, São Paulo-SP, 01000-000",
                                "CLIENTE");

                when(cadastrarUsuarioUseCase.execute(any(CadastrarUsuarioUseCase.InputModel.class)))
                                .thenReturn(output);

                mockMvc.perform(post("/api/usuarios")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.nome").value("João Silva"))
                                .andExpect(jsonPath("$.email").value("joao@email.com"))
                                .andExpect(jsonPath("$.login").value("joao"))
                                .andExpect(jsonPath("$.tipoUsuario").value("CLIENTE"));

                verify(cadastrarUsuarioUseCase).execute(any(CadastrarUsuarioUseCase.InputModel.class));
        }

        @Test
        @WithMockUser
        @DisplayName("Deve atualizar usuário com autenticação")
        void deveAtualizarUsuarioComAutenticacao() throws Exception {
                AtualizarUsuarioDTO dto = new AtualizarUsuarioDTO(
                                "João Silva Santos",
                                "joao.novo@email.com",
                                "joao_novo",
                                "Rua B",
                                "200",
                                "Apto 10",
                                "Jardins",
                                "São Paulo",
                                "SP",
                                "02000-000");

                AtualizarUsuarioUseCase.OutputModel output = new AtualizarUsuarioUseCase.OutputModel(
                                1L,
                                "João Silva Santos",
                                "joao.novo@email.com",
                                "joao_novo",
                                "Rua B, 200, Apto 10, Jardins, São Paulo-SP, 02000-000",
                                "CLIENTE");

                when(atualizarUsuarioUseCase.execute(eq(1L), any(AtualizarUsuarioUseCase.InputModel.class)))
                                .thenReturn(output);

                mockMvc.perform(put("/api/usuarios/1")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.nome").value("João Silva Santos"))
                                .andExpect(jsonPath("$.email").value("joao.novo@email.com"));

                verify(atualizarUsuarioUseCase).execute(eq(1L), any(AtualizarUsuarioUseCase.InputModel.class));
        }

        @Test
        @WithMockUser
        @DisplayName("Deve excluir usuário com autenticação")
        void deveExcluirUsuarioComAutenticacao() throws Exception {
                doNothing().when(excluirUsuarioUseCase).execute(1L);

                mockMvc.perform(delete("/api/usuarios/1")
                                .with(csrf()))
                                .andExpect(status().isNoContent());

                verify(excluirUsuarioUseCase).execute(1L);
        }

        @Test
        @WithMockUser
        @DisplayName("Deve trocar senha do usuário")
        void deveTrocarSenhaDoUsuario() throws Exception {
                TrocarSenhaDTO dto = new TrocarSenhaDTO(
                                "senhaAtual123",
                                "novaSenha456",
                                "novaSenha456");

                doNothing().when(trocarSenhaUseCase).execute(eq(1L), any(TrocarSenhaUseCase.InputModel.class));

                mockMvc.perform(patch("/api/usuarios/1/trocar-senha")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isNoContent());

                verify(trocarSenhaUseCase).execute(eq(1L), any(TrocarSenhaUseCase.InputModel.class));
        }

        @Test
        @WithMockUser
        @DisplayName("Deve listar todos os usuários")
        void deveListarTodosUsuarios() throws Exception {
                var output1 = new ListarTodosUsuariosUseCase.OutputModel(
                                1L, "João Silva", "joao@email.com", "joao", "Endereço 1", "CLIENTE");
                var output2 = new ListarTodosUsuariosUseCase.OutputModel(
                                2L, "Maria Santos", "maria@email.com", "maria", "Endereço 2", "DONO_RESTAURANTE");

                when(listarTodosUsuariosUseCase.execute()).thenReturn(List.of(output1, output2));

                mockMvc.perform(get("/api/usuarios"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$.length()").value(2))
                                .andExpect(jsonPath("$[0].nome").value("João Silva"))
                                .andExpect(jsonPath("$[1].nome").value("Maria Santos"));

                verify(listarTodosUsuariosUseCase).execute();
        }

        @Test
        @WithMockUser
        @DisplayName("Deve associar tipo do usuário")
        void deveAssociarTipoDoUsuario() throws Exception {
                AssociarTipoDoUsuarioDTO dto = new AssociarTipoDoUsuarioDTO("DONO_RESTAURANTE");

                AssociarTipoDoUsuarioUseCase.OutputModel output = new AssociarTipoDoUsuarioUseCase.OutputModel(
                                1L,
                                "João Silva",
                                "joao@email.com",
                                "joao",
                                "DONO_RESTAURANTE");

                when(associarTipoDoUsuarioUseCase.execute(eq(1L), any(AssociarTipoDoUsuarioUseCase.InputModel.class)))
                                .thenReturn(output);

                mockMvc.perform(patch("/api/usuarios/1/tipo")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.tipoUsuario").value("DONO_RESTAURANTE"));

                verify(associarTipoDoUsuarioUseCase).execute(eq(1L),
                                any(AssociarTipoDoUsuarioUseCase.InputModel.class));
        }

        @Test
        @WithMockUser
        @DisplayName("Deve buscar usuários por nome")
        void deveBuscarUsuariosPorNome() throws Exception {
                var output1 = new BuscarUsuariosPorNomeUseCase.OutputModel(
                                1L, "João Silva", "joao@email.com", "joao", "Endereço 1", "CLIENTE");
                var output2 = new BuscarUsuariosPorNomeUseCase.OutputModel(
                                2L, "João Pedro", "joaop@email.com", "joaop", "Endereço 2", "CLIENTE");

                when(buscarUsuariosPorNomeUseCase.execute("João")).thenReturn(List.of(output1, output2));

                mockMvc.perform(get("/api/usuarios/search")
                                .param("nome", "João"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$.length()").value(2))
                                .andExpect(jsonPath("$[0].nome").value("João Silva"))
                                .andExpect(jsonPath("$[1].nome").value("João Pedro"));

                verify(buscarUsuariosPorNomeUseCase).execute("João");
        }

        @Test
        @DisplayName("Deve cadastrar usuário do tipo DONO_RESTAURANTE")
        void deveCadastrarUsuarioDonoRestaurante() throws Exception {
                NovoUsuarioDTO dto = new NovoUsuarioDTO(
                                "Maria Silva",
                                "maria@email.com",
                                "maria",
                                "senha456",
                                "Rua B", "200", "", "Centro", "São Paulo", "SP", "02000-000",
                                "DONO_RESTAURANTE");

                CadastrarUsuarioUseCase.OutputModel output = new CadastrarUsuarioUseCase.OutputModel(
                                2L, "Maria Silva", "maria@email.com", "maria", "Endereço", "DONO_RESTAURANTE");

                when(cadastrarUsuarioUseCase.execute(any(CadastrarUsuarioUseCase.InputModel.class)))
                                .thenReturn(output);

                mockMvc.perform(post("/api/usuarios")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.tipoUsuario").value("DONO_RESTAURANTE"));
        }

        @Test
        @WithMockUser
        @DisplayName("Deve retornar lista vazia ao buscar por nome inexistente")
        void deveRetornarListaVaziaAoBuscarPorNomeInexistente() throws Exception {
                when(buscarUsuariosPorNomeUseCase.execute("NomeInexistente")).thenReturn(List.of());

                mockMvc.perform(get("/api/usuarios/search")
                                .param("nome", "NomeInexistente"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$.length()").value(0));
        }

        @Test
        @WithMockUser
        @DisplayName("Deve retornar lista vazia quando não há usuários")
        void deveRetornarListaVaziaQuandoNaoHaUsuarios() throws Exception {
                when(listarTodosUsuariosUseCase.execute()).thenReturn(List.of());

                mockMvc.perform(get("/api/usuarios"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$.length()").value(0));
        }
}