package br.com.fiap.infra.controllers;

import br.com.fiap.core.usecases.restaurante.*;
import br.com.fiap.infra.dto.AtualizarRestauranteDTO;
import br.com.fiap.infra.dto.NovoRestauranteDTO;
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

@WebMvcTest(RestauranteController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@DisplayName("RestauranteController - Testes Unitários")
class RestauranteControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private JwtTokenService jwtTokenService;

        @MockitoBean
        private UserDetailsService userDetailsService;

        @MockitoBean
        private CadastrarRestauranteUseCase cadastrarRestauranteUseCase;

        @MockitoBean
        private ObterRestaurantePorIdUseCase obterRestaurantePorIdUseCase;

        @MockitoBean
        private ListarTodosRestaurantesUseCase listarTodosRestaurantesUseCase;

        @MockitoBean
        private AtualizarRestauranteUseCase atualizarRestauranteUseCase;

        @MockitoBean
        private DeletarRestauranteUseCase deletarRestauranteUseCase;

        @Test
        @WithMockUser
        @DisplayName("Deve cadastrar restaurante com sucesso")
        void deveCadastrarRestauranteComSucesso() throws Exception {
                NovoRestauranteDTO dto = new NovoRestauranteDTO(
                                "Restaurante Italiano",
                                "Rua A",
                                "100",
                                "Sala 5",
                                "Centro",
                                "São Paulo",
                                "SP",
                                "01000-000",
                                "ITALIANA",
                                "08:00-22:00");

                var output = new CadastrarRestauranteUseCase.OutputModel(
                                1L,
                                "Restaurante Italiano",
                                "Rua A, 100, Sala 5, Centro, São Paulo-SP, 01000-000",
                                "ITALIANA",
                                "08:00-22:00");

                when(cadastrarRestauranteUseCase.execute(any(CadastrarRestauranteUseCase.InputModel.class)))
                                .thenReturn(output);

                mockMvc.perform(post("/api/restaurantes")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.nome").value("Restaurante Italiano"))
                                .andExpect(jsonPath("$.tipoCozinha").value("ITALIANA"))
                                .andExpect(jsonPath("$.horarioFuncionamento").value("08:00-22:00"));

                verify(cadastrarRestauranteUseCase).execute(any(CadastrarRestauranteUseCase.InputModel.class));
        }

        @Test
        @WithMockUser
        @DisplayName("Deve obter restaurante por ID com sucesso")
        void deveObterRestaurantePorIdComSucesso() throws Exception {
                var output = new ObterRestaurantePorIdUseCase.OutputModel(
                                1L,
                                "Restaurante Italiano",
                                "Rua A, 100, Centro, São Paulo-SP, 01000-000",
                                "ITALIANA",
                                "08:00-22:00");

                when(obterRestaurantePorIdUseCase.execute(1L)).thenReturn(output);

                mockMvc.perform(get("/api/restaurantes/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.nome").value("Restaurante Italiano"))
                                .andExpect(jsonPath("$.tipoCozinha").value("ITALIANA"));

                verify(obterRestaurantePorIdUseCase).execute(1L);
        }

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 404 quando restaurante não existe")
        void deveRetornar404QuandoRestauranteNaoExiste() throws Exception {
                when(obterRestaurantePorIdUseCase.execute(999L)).thenReturn(null);

                mockMvc.perform(get("/api/restaurantes/999"))
                                .andExpect(status().isNotFound());

                verify(obterRestaurantePorIdUseCase).execute(999L);
        }

        @Test
        @WithMockUser
        @DisplayName("Deve listar todos os restaurantes")
        void deveListarTodosRestaurantes() throws Exception {
                var output1 = new ListarTodosRestaurantesUseCase.OutputModel(
                                1L, "Restaurante Italiano", "Endereço 1", "ITALIANA", "08:00-22:00");
                var output2 = new ListarTodosRestaurantesUseCase.OutputModel(
                                2L, "Restaurante Japonês", "Endereço 2", "JAPONESA", "12:00-23:00");

                when(listarTodosRestaurantesUseCase.execute()).thenReturn(List.of(output1, output2));

                mockMvc.perform(get("/api/restaurantes"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$.length()").value(2))
                                .andExpect(jsonPath("$[0].nome").value("Restaurante Italiano"))
                                .andExpect(jsonPath("$[1].nome").value("Restaurante Japonês"));

                verify(listarTodosRestaurantesUseCase).execute();
        }

        @Test
        @WithMockUser
        @DisplayName("Deve atualizar restaurante com sucesso")
        void deveAtualizarRestauranteComSucesso() throws Exception {
                AtualizarRestauranteDTO dto = new AtualizarRestauranteDTO(
                                "Restaurante Italiano Atualizado",
                                "Rua B",
                                "200",
                                "Loja 10",
                                "Jardins",
                                "São Paulo",
                                "SP",
                                "02000-000",
                                "ITALIANA",
                                "10:00-23:00");

                AtualizarRestauranteUseCase.OutputModel output = new AtualizarRestauranteUseCase.OutputModel(
                                1L,
                                "Restaurante Italiano Atualizado",
                                "Rua B, 200, Loja 10, Jardins, São Paulo-SP, 02000-000",
                                "ITALIANA",
                                "10:00-23:00");

                when(atualizarRestauranteUseCase.execute(eq(1L), any(AtualizarRestauranteUseCase.InputModel.class)))
                                .thenReturn(Optional.of(output));

                mockMvc.perform(put("/api/restaurantes/1")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.nome").value("Restaurante Italiano Atualizado"))
                                .andExpect(jsonPath("$.horarioFuncionamento").value("10:00-23:00"));

                verify(atualizarRestauranteUseCase).execute(eq(1L), any(AtualizarRestauranteUseCase.InputModel.class));
        }

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 404 ao atualizar restaurante inexistente")
        void deveRetornar404AoAtualizarRestauranteInexistente() throws Exception {
                AtualizarRestauranteDTO dto = new AtualizarRestauranteDTO(
                                "Restaurante", "Rua", "100", "", "Centro", "SP", "SP", "01000-000", "ITALIANA",
                                "08:00-22:00");

                when(atualizarRestauranteUseCase.execute(eq(999L), any(AtualizarRestauranteUseCase.InputModel.class)))
                                .thenReturn(Optional.empty());

                mockMvc.perform(put("/api/restaurantes/999")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser
        @DisplayName("Deve deletar restaurante com sucesso")
        void deveDeletarRestauranteComSucesso() throws Exception {
                Long id = 1L;
                var restauranteExistente = new ObterRestaurantePorIdUseCase.OutputModel(
                                id, "Restaurante Japonês", "Endereco", "JAPONESA", "08:00-22:00");

                when(obterRestaurantePorIdUseCase.execute(id)).thenReturn(restauranteExistente);
                doNothing().when(deletarRestauranteUseCase).execute(id);

                mockMvc.perform(delete("/api/restaurantes/{id}", id)
                                .with(csrf()))
                                .andExpect(status().isNoContent());

                verify(deletarRestauranteUseCase).execute(id);
        }

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 404 ao deletar restaurante inexistente")
        void deveRetornar404AoDeletarRestauranteInexistente() throws Exception {
                when(obterRestaurantePorIdUseCase.execute(999L)).thenReturn(null);

                mockMvc.perform(delete("/api/restaurantes/999")
                                .with(csrf()))
                                .andExpect(status().isNotFound());

                verify(deletarRestauranteUseCase, never()).execute(999L);
        }

        @Test
        @WithMockUser
        @DisplayName("Deve cadastrar restaurante com diferentes tipos de cozinha")
        void deveCadastrarRestauranteComDiferentesTiposCozinha() throws Exception {
                NovoRestauranteDTO dto = new NovoRestauranteDTO(
                                "Restaurante Japonês",
                                "Rua C", "300", "", "Vila Nova", "São Paulo", "SP", "03000-000",
                                "JAPONESA",
                                "12:00-23:00");

                CadastrarRestauranteUseCase.OutputModel output = new CadastrarRestauranteUseCase.OutputModel(
                                2L, "Restaurante Japonês", "Endereço", "JAPONESA", "12:00-23:00");

                when(cadastrarRestauranteUseCase.execute(any(CadastrarRestauranteUseCase.InputModel.class)))
                                .thenReturn(output);

                mockMvc.perform(post("/api/restaurantes")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.tipoCozinha").value("JAPONESA"));
        }

        @Test
        @WithMockUser
        @DisplayName("Deve retornar lista vazia quando não há restaurantes")
        void deveRetornarListaVaziaQuandoNaoHaRestaurantes() throws Exception {
                when(listarTodosRestaurantesUseCase.execute()).thenReturn(List.of());

                mockMvc.perform(get("/api/restaurantes"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$.length()").value(0));
        }
}