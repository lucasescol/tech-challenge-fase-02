package br.com.fiap.infra.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("RestauranteController Integration Tests")
class RestauranteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("POST /api/v1/restaurantes - Deve cadastrar restaurante com dados válidos")
    @WithMockUser
    void deveCadastrarRestauranteComDadosValidos() throws Exception {
        String jsonRequest = """
                {
                    "nome": "Cantina do Luigi",
                    "logradouro": "Rua A",
                    "numero": "123",
                    "complemento": "Apt 456",
                    "bairro": "Centro",
                    "cidade": "São Paulo",
                    "estado": "SP",
                    "cep": "01310100",
                    "tipoCozinha": "ITALIANA",
                    "horarioFuncionamento": "09:00-23:00"
                }
                """;

        mockMvc.perform(post("/api/v1/restaurantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /api/v1/restaurantes - Deve retornar 400 para nome vazio")
    void deveRetornarBadRequestParaNomeVazio() throws Exception {
        String jsonRequest = """
                {
                    "nome": "",
                    "logradouro": "Rua A",
                    "numero": "123",
                    "complemento": "",
                    "bairro": "Centro",
                    "cidade": "São Paulo",
                    "estado": "SP",
                    "cep": "01310100",
                    "tipoCozinha": "ITALIANA",
                    "horarioFuncionamento": "09:00-23:00"
                }
                """;

        mockMvc.perform(post("/api/v1/restaurantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/v1/restaurantes - Deve retornar 400 para CEP inválido")
    void deveRetornarBadRequestParaCepInvalido() throws Exception {
        String jsonRequest = """
                {
                    "nome": "Cantina do Luigi",
                    "logradouro": "Rua A",
                    "numero": "123",
                    "complemento": "",
                    "bairro": "Centro",
                    "cidade": "São Paulo",
                    "estado": "SP",
                    "cep": "01310",
                    "tipoCozinha": "ITALIANA",
                    "horarioFuncionamento": "09:00-23:00"
                }
                """;

        mockMvc.perform(post("/api/v1/restaurantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/v1/restaurantes - Deve retornar 400 para tipo de cozinha inválido")
    void deveRetornarBadRequestParaTipoCozinhaInvalido() throws Exception {
        String jsonRequest = """
                {
                    "nome": "Cantina do Luigi",
                    "logradouro": "Rua A",
                    "numero": "123",
                    "complemento": "",
                    "bairro": "Centro",
                    "cidade": "São Paulo",
                    "estado": "SP",
                    "cep": "01310100",
                    "tipoCozinha": "DESCONHECIDA",
                    "horarioFuncionamento": "09:00-23:00"
                }
                """;

        mockMvc.perform(post("/api/v1/restaurantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/v1/restaurantes - Deve aceitar restaurante sem complemento")
    void deveAceitarRestauranteSemComplemento() throws Exception {
        String jsonRequest = """
                {
                    "nome": "Cantina do Luigi",
                    "logradouro": "Rua A",
                    "numero": "123",
                    "complemento": "",
                    "bairro": "Centro",
                    "cidade": "São Paulo",
                    "estado": "SP",
                    "cep": "01310100",
                    "tipoCozinha": "ITALIANA",
                    "horarioFuncionamento": "09:00-23:00"
                }
                """;

        mockMvc.perform(post("/api/v1/restaurantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated());
    }
}
