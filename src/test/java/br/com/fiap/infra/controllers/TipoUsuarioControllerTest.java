package br.com.fiap.infra.controllers;

import br.com.fiap.core.usecases.tipo_usuario.*;
import br.com.fiap.infra.dto.AtualizarTipoUsuarioDTO;
import br.com.fiap.infra.dto.NovoTipoUsuarioDTO;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TipoUsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@DisplayName("TipoUsuarioController - Testes Unitários")
class TipoUsuarioControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private JwtTokenService jwtTokenService;

        @MockitoBean
        private UserDetailsService userDetailsService;

        @MockitoBean
        private CadastrarTipoUsuarioUseCase cadastrarTipoUsuarioUseCase;

        @MockitoBean
        private ObterTipoUsuarioPorIdUseCase obterTipoUsuarioPorIdUseCase;

        @MockitoBean
        private ListarTodosTiposUsuarioUseCase listarTodosTiposUsuarioUseCase;

        @MockitoBean
        private AtualizarTipoUsuarioUseCase atualizarTipoUsuarioUseCase;

        @MockitoBean
        private DeletarTipoUsuarioUseCase deletarTipoUsuarioUseCase;

        @Test
        @WithMockUser
        @DisplayName("Deve cadastrar tipo de usuário com sucesso")
        void deveCadastrarTipoUsuarioComSucesso() throws Exception {
                NovoTipoUsuarioDTO dto = new NovoTipoUsuarioDTO("CLIENTE", "Cliente do sistema");

                CadastrarTipoUsuarioUseCase.OutputModel output = new CadastrarTipoUsuarioUseCase.OutputModel(
                                1L,
                                "CLIENTE",
                                "Cliente do sistema");

                when(cadastrarTipoUsuarioUseCase.execute(any(CadastrarTipoUsuarioUseCase.InputModel.class)))
                                .thenReturn(output);

                mockMvc.perform(post("/api/tipos-usuario")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.nome").value("CLIENTE"))
                                .andExpect(jsonPath("$.descricao").value("Cliente do sistema"));

                verify(cadastrarTipoUsuarioUseCase).execute(any(CadastrarTipoUsuarioUseCase.InputModel.class));
        }

        @Test
        @WithMockUser
        @DisplayName("Deve obter tipo de usuário por ID com sucesso")
        void deveObterTipoUsuarioPorIdComSucesso() throws Exception {
                ObterTipoUsuarioPorIdUseCase.OutputModel output = new ObterTipoUsuarioPorIdUseCase.OutputModel(
                                1L,
                                "CLIENTE",
                                "Cliente do sistema");

                when(obterTipoUsuarioPorIdUseCase.execute(1L)).thenReturn(Optional.of(output));

                mockMvc.perform(get("/api/tipos-usuario/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.nome").value("CLIENTE"))
                                .andExpect(jsonPath("$.descricao").value("Cliente do sistema"));

                verify(obterTipoUsuarioPorIdUseCase).execute(1L);
        }

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 404 quando tipo de usuário não existe")
        void deveRetornar404QuandoTipoUsuarioNaoExiste() throws Exception {
                when(obterTipoUsuarioPorIdUseCase.execute(999L)).thenReturn(Optional.empty());

                mockMvc.perform(get("/api/tipos-usuario/999"))
                                .andExpect(status().isNotFound());

                verify(obterTipoUsuarioPorIdUseCase).execute(999L);
        }

        @Test
        @WithMockUser
        @DisplayName("Deve listar todos os tipos de usuário")
        void deveListarTodosTiposUsuario() throws Exception {
                var output1 = new ListarTodosTiposUsuarioUseCase.OutputModel(
                                1L, "CLIENTE", "Cliente");
                var output2 = new ListarTodosTiposUsuarioUseCase.OutputModel(
                                2L, "DONO_RESTAURANTE", "Dono de Restaurante");
                var output3 = new ListarTodosTiposUsuarioUseCase.OutputModel(
                                3L, "ADMINISTRADOR", "Administrador");

                when(listarTodosTiposUsuarioUseCase.execute()).thenReturn(List.of(output1, output2, output3));

                mockMvc.perform(get("/api/tipos-usuario"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$.length()").value(3))
                                .andExpect(jsonPath("$[0].nome").value("CLIENTE"))
                                .andExpect(jsonPath("$[1].nome").value("DONO_RESTAURANTE"))
                                .andExpect(jsonPath("$[2].nome").value("ADMINISTRADOR"));

                verify(listarTodosTiposUsuarioUseCase).execute();
        }

        @Test
        @WithMockUser
        @DisplayName("Deve atualizar tipo de usuário com sucesso")
        void deveAtualizarTipoUsuarioComSucesso() throws Exception {
                AtualizarTipoUsuarioDTO dto = new AtualizarTipoUsuarioDTO(
                                "CLIENTE_VIP",
                                "Cliente VIP do sistema");

                AtualizarTipoUsuarioUseCase.OutputModel output = new AtualizarTipoUsuarioUseCase.OutputModel(
                                1L,
                                "CLIENTE_VIP",
                                "Cliente VIP do sistema");

                when(atualizarTipoUsuarioUseCase.execute(eq(1L), any(AtualizarTipoUsuarioUseCase.InputModel.class)))
                                .thenReturn(Optional.of(output));

                mockMvc.perform(put("/api/tipos-usuario/1")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.nome").value("CLIENTE_VIP"))
                                .andExpect(jsonPath("$.descricao").value("Cliente VIP do sistema"));

                verify(atualizarTipoUsuarioUseCase).execute(eq(1L), any(AtualizarTipoUsuarioUseCase.InputModel.class));
        }

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 404 ao atualizar tipo de usuário inexistente")
        void deveRetornar404AoAtualizarTipoUsuarioInexistente() throws Exception {
                AtualizarTipoUsuarioDTO dto = new AtualizarTipoUsuarioDTO("TIPO", "Descrição");

                when(atualizarTipoUsuarioUseCase.execute(eq(999L), any(AtualizarTipoUsuarioUseCase.InputModel.class)))
                                .thenReturn(Optional.empty());

                mockMvc.perform(put("/api/tipos-usuario/999")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser
        @DisplayName("Deve deletar tipo de usuário com sucesso")
        void deveDeletarTipoUsuarioComSucesso() throws Exception {
                when(deletarTipoUsuarioUseCase.execute(1L)).thenReturn(true);

                mockMvc.perform(delete("/api/tipos-usuario/1")
                                .with(csrf()))
                                .andExpect(status().isNoContent());

                verify(deletarTipoUsuarioUseCase).execute(1L);
        }

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 404 ao deletar tipo de usuário inexistente")
        void deveRetornar404AoDeletarTipoUsuarioInexistente() throws Exception {
                when(deletarTipoUsuarioUseCase.execute(999L)).thenReturn(false);

                mockMvc.perform(delete("/api/tipos-usuario/999")
                                .with(csrf()))
                                .andExpect(status().isNotFound());

                verify(deletarTipoUsuarioUseCase).execute(999L);
        }

        @Test
        @WithMockUser
        @DisplayName("Deve cadastrar diferentes tipos de usuário")
        void deveCadastrarDiferentesTiposUsuario() throws Exception {
                NovoTipoUsuarioDTO dto = new NovoTipoUsuarioDTO(
                                "FUNCIONARIO",
                                "Funcionário do restaurante");

                CadastrarTipoUsuarioUseCase.OutputModel output = new CadastrarTipoUsuarioUseCase.OutputModel(
                                4L, "FUNCIONARIO", "Funcionário do restaurante");

                when(cadastrarTipoUsuarioUseCase.execute(any(CadastrarTipoUsuarioUseCase.InputModel.class)))
                                .thenReturn(output);

                mockMvc.perform(post("/api/tipos-usuario")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.nome").value("FUNCIONARIO"));
        }

        @Test
        @WithMockUser
        @DisplayName("Deve retornar lista vazia quando não há tipos de usuário")
        void deveRetornarListaVaziaQuandoNaoHaTiposUsuario() throws Exception {
                when(listarTodosTiposUsuarioUseCase.execute()).thenReturn(List.of());

                mockMvc.perform(get("/api/tipos-usuario"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$.length()").value(0));
        }
}