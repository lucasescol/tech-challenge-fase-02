package br.com.fiap.infra.controllers;

import br.com.fiap.core.usecases.usuario.AutenticarUsuarioUseCase;
import br.com.fiap.infra.dto.LoginRequestDTO;
import br.com.fiap.infra.services.JwtTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@DisplayName("AuthController - Testes Unitários")
class AuthControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private JwtTokenService jwtTokenService;

        @MockitoBean
        private UserDetailsService userDetailsService;

        @MockitoBean
        private AutenticarUsuarioUseCase autenticarUsuarioUseCase;

        @Test
        @DisplayName("Deve realizar login com sucesso")
        void deveRealizarLoginComSucesso() throws Exception {
                LoginRequestDTO request = new LoginRequestDTO("joao", "senha123");

                AutenticarUsuarioUseCase.OutputModel output = new AutenticarUsuarioUseCase.OutputModel(
                                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                                "Bearer",
                                "joao",
                                "Joao", "joao@teste.com", "CLIENTE");

                when(autenticarUsuarioUseCase.execute(any(AutenticarUsuarioUseCase.InputModel.class)))
                                .thenReturn(output);

                mockMvc.perform(post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.token").value("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."))
                                .andExpect(jsonPath("$.tipo").value("Bearer"))
                                .andExpect(jsonPath("$.login").value("joao"))
                                .andExpect(jsonPath("$.tipoUsuario").value("CLIENTE"));

                verify(autenticarUsuarioUseCase).execute(any(AutenticarUsuarioUseCase.InputModel.class));
        }

        @Test
        @DisplayName("Deve retornar 400 quando login estiver vazio")
        void deveRetornar400QuandoLoginVazio() throws Exception {
                LoginRequestDTO request = new LoginRequestDTO("", "senha123");

                mockMvc.perform(post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Deve retornar 400 quando senha estiver vazia")
        void deveRetornar400QuandoSenhaVazia() throws Exception {
                LoginRequestDTO request = new LoginRequestDTO("joao", "");

                mockMvc.perform(post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Deve autenticar usuário DONO_RESTAURANTE corretamente")
        void deveAutenticarDonoRestaurante() throws Exception {
                LoginRequestDTO request = new LoginRequestDTO("maria", "senha456");

                AutenticarUsuarioUseCase.OutputModel output = new AutenticarUsuarioUseCase.OutputModel(
                                "token-dono",
                                "Bearer",
                                "maria",
                                "Maria", "maria@teste.com", "DONO_RESTAURANTE");

                when(autenticarUsuarioUseCase.execute(any(AutenticarUsuarioUseCase.InputModel.class)))
                                .thenReturn(output);

                mockMvc.perform(post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.tipoUsuario").value("DONO_RESTAURANTE"));
        }

        @Test
        @DisplayName("Deve autenticar usuário ADMINISTRADOR corretamente")
        void deveAutenticarAdministrador() throws Exception {
                LoginRequestDTO request = new LoginRequestDTO("admin", "admin123");

                AutenticarUsuarioUseCase.OutputModel output = new AutenticarUsuarioUseCase.OutputModel(
                                "token-admin",
                                "Bearer",
                                "admin",
                                "admin", "admin@admin.com", "ADMINISTRADOR");

                when(autenticarUsuarioUseCase.execute(any(AutenticarUsuarioUseCase.InputModel.class)))
                                .thenReturn(output);

                mockMvc.perform(post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.tipoUsuario").value("ADMINISTRADOR"));
        }

        @Test
        @DisplayName("Deve retornar Content-Type application/json")
        void deveRetornarContentTypeJson() throws Exception {
                LoginRequestDTO request = new LoginRequestDTO("joao", "senha123");

                AutenticarUsuarioUseCase.OutputModel output = new AutenticarUsuarioUseCase.OutputModel(
                                "token",
                                "Bearer",
                                "joao",
                                "Joao", "joao@teste.com", "CLIENTE");

                when(autenticarUsuarioUseCase.execute(any(AutenticarUsuarioUseCase.InputModel.class)))
                                .thenReturn(output);

                mockMvc.perform(post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }
}