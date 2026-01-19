package br.com.fiap.infra.controllers;

import br.com.fiap.core.domain.CardapioItem;
import br.com.fiap.core.usecases.cardapio_item.*;
import br.com.fiap.infra.dto.AtualizarCardapioItemDTO;
import br.com.fiap.infra.dto.NovoCardapioItemDTO;
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

@WebMvcTest(CardapioItemController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@DisplayName("CardapioItemController - Testes Unitários")
class CardapioItemControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private JwtTokenService jwtTokenService;

        @MockitoBean
        private UserDetailsService userDetailsService;

        @MockitoBean
        private CadastrarCardapioItemUseCase cadastrarCardapioItemUseCase;

        @MockitoBean
        private ObterCardapioItemPorIdUseCase obterCardapioItemPorIdUseCase;

        @MockitoBean
        private ListarCardapioItensPorRestauranteUseCase listarCardapioItensPorRestauranteUseCase;

        @MockitoBean
        private ListarTodosCardapioItensUseCase listarTodosCardapioItensUseCase;

        @MockitoBean
        private AtualizarCardapioItemUseCase atualizarCardapioItemUseCase;

        @MockitoBean
        private DeletarCardapioItemUseCase deletarCardapioItemUseCase;

        @Test
        @DisplayName("Deve cadastrar item do cardápio com sucesso")
        void deveCadastrarItemComSucesso() throws Exception {
                NovoCardapioItemDTO dto = new NovoCardapioItemDTO(
                                1L,
                                "Pizza Margherita",
                                "Deliciosa pizza com molho de tomate, mussarela e manjericão",
                                45.90,
                                false,
                                "/images/pizza.jpg");

                CardapioItem itemSalvo = CardapioItem.criar(
                                1L, 1L, "Pizza Margherita",
                                "Deliciosa pizza com molho de tomate, mussarela e manjericão",
                                45.90, false, "/images/pizza.jpg");

                when(cadastrarCardapioItemUseCase.execute(any(CardapioItem.class))).thenReturn(itemSalvo);

                mockMvc.perform(post("/api/cardapio-itens")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.nome").value("Pizza Margherita"))
                                .andExpect(jsonPath("$.preco").value(45.90));

                verify(cadastrarCardapioItemUseCase).execute(any(CardapioItem.class));
        }

        @Test
        @WithMockUser
        @DisplayName("Deve obter item do cardápio por ID")
        void deveObterItemPorId() throws Exception {
                var output = new ObterCardapioItemPorIdUseCase.OutputModel(
                                1L, 1L, "Pizza Margherita",
                                "Deliciosa pizza", 45.90, false, "/images/pizza.jpg");

                when(obterCardapioItemPorIdUseCase.execute(1L)).thenReturn(Optional.of(output));

                mockMvc.perform(get("/api/cardapio-itens/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.nome").value("Pizza Margherita"));

                verify(obterCardapioItemPorIdUseCase).execute(1L);
        }

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 404 quando item não existe")
        void deveRetornar404QuandoItemNaoExiste() throws Exception {
                when(obterCardapioItemPorIdUseCase.execute(999L)).thenReturn(Optional.empty());

                mockMvc.perform(get("/api/cardapio-itens/999"))
                                .andExpect(status().isNotFound());

                verify(obterCardapioItemPorIdUseCase).execute(999L);
        }

        @Test
        @WithMockUser
        @DisplayName("Deve listar itens por restaurante")
        void deveListarItensPorRestaurante() throws Exception {
                var output1 = new ListarCardapioItensPorRestauranteUseCase.OutputModel(
                                1L, 1L, "Pizza", "Desc", 40.0, false, "/img1.jpg");
                var output2 = new ListarCardapioItensPorRestauranteUseCase.OutputModel(
                                2L, 1L, "Lasanha", "Desc", 35.0, true, "/img2.jpg");

                when(listarCardapioItensPorRestauranteUseCase.execute(1L))
                                .thenReturn(List.of(output1, output2));

                mockMvc.perform(get("/api/cardapio-itens/restaurante/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$.length()").value(2))
                                .andExpect(jsonPath("$[0].nome").value("Pizza"))
                                .andExpect(jsonPath("$[1].nome").value("Lasanha"));

                verify(listarCardapioItensPorRestauranteUseCase).execute(1L);
        }

        @Test
        @WithMockUser
        @DisplayName("Deve listar todos os itens")
        void deveListarTodosItens() throws Exception {
                var output1 = new ListarTodosCardapioItensUseCase.OutputModel(
                                1L, 1L, "Pizza", "Desc", 40.0, false, "/img1.jpg");
                var output2 = new ListarTodosCardapioItensUseCase.OutputModel(
                                2L, 2L, "Sushi", "Desc", 50.0, false, "/img2.jpg");

                when(listarTodosCardapioItensUseCase.execute())
                                .thenReturn(List.of(output1, output2));

                mockMvc.perform(get("/api/cardapio-itens"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$.length()").value(2));

                verify(listarTodosCardapioItensUseCase).execute();
        }

        @Test
        @WithMockUser
        @DisplayName("Deve atualizar item do cardápio")
        void deveAtualizarItem() throws Exception {
                AtualizarCardapioItemDTO dto = new AtualizarCardapioItemDTO(
                                "Pizza Atualizada",
                                "Nova descrição",
                                50.0,
                                true,
                                "/img-nova.jpg");

                var output = new AtualizarCardapioItemUseCase.OutputModel(
                                1L, 1L, "Pizza Atualizada", "Nova descrição", 50.0, true, "/img-nova.jpg");

                when(atualizarCardapioItemUseCase.execute(eq(1L), any(AtualizarCardapioItemUseCase.InputModel.class)))
                                .thenReturn(Optional.of(output));

                mockMvc.perform(put("/api/cardapio-itens/1")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.nome").value("Pizza Atualizada"))
                                .andExpect(jsonPath("$.preco").value(50.0));

                verify(atualizarCardapioItemUseCase).execute(eq(1L),
                                any(AtualizarCardapioItemUseCase.InputModel.class));
        }

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 404 ao atualizar item inexistente")
        void deveRetornar404AoAtualizarItemInexistente() throws Exception {
                AtualizarCardapioItemDTO dto = new AtualizarCardapioItemDTO(
                                "Pizza", "Desc", 40.0, false, "/img.jpg");

                when(atualizarCardapioItemUseCase.execute(eq(999L), any(AtualizarCardapioItemUseCase.InputModel.class)))
                                .thenReturn(Optional.empty());

                mockMvc.perform(put("/api/cardapio-itens/999")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser
        @DisplayName("Deve deletar item do cardápio")
        void deveDeletarItem() throws Exception {
                doNothing().when(deletarCardapioItemUseCase).execute(1L);

                mockMvc.perform(delete("/api/cardapio-itens/1")
                                .with(csrf()))
                                .andExpect(status().isNoContent());

                verify(deletarCardapioItemUseCase).execute(1L);
        }

        @Test
        @WithMockUser
        @DisplayName("Deve retornar lista vazia quando não há itens do restaurante")
        void deveRetornarListaVaziaQuandoNaoHaItens() throws Exception {
                when(listarCardapioItensPorRestauranteUseCase.execute(1L))
                                .thenReturn(List.of());

                mockMvc.perform(get("/api/cardapio-itens/restaurante/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$.length()").value(0));
        }
}