package br.com.fiap.infra.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import br.com.fiap.core.domain.Restaurante;
import br.com.fiap.core.gateways.IRestauranteGateway;
import br.com.fiap.core.usecases.CadastrarRestauranteUseCase;
import br.com.fiap.infra.dto.NovoRestauranteDTO;
import br.com.fiap.infra.dto.RestauranteResponseDTO;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/restaurantes")
@Tag(name = "Restaurantes", description = "Endpoints para gerenciamento de restaurantes")
public class RestauranteController {

        private final CadastrarRestauranteUseCase cadastrarRestauranteUseCase;
        private final IRestauranteGateway restauranteGateway;

        public RestauranteController(CadastrarRestauranteUseCase cadastrarRestauranteUseCase,
                        IRestauranteGateway restauranteGateway) {
                this.cadastrarRestauranteUseCase = cadastrarRestauranteUseCase;
                this.restauranteGateway = restauranteGateway;
        }

        @PostMapping
        @Operation(summary = "Cadastrar novo restaurante", description = "Cadastra um novo restaurante no sistema")
        @ApiResponse(responseCode = "201", description = "Restaurante criado com sucesso")
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
        public ResponseEntity<RestauranteResponseDTO> cadastrar(@Valid @RequestBody NovoRestauranteDTO dto) {

                Restaurante novoRestaurante = Restaurante.create(
                                null,
                                dto.nome(),
                                dto.logradouro(),
                                dto.numero(),
                                dto.complemento(),
                                dto.bairro(),
                                dto.cidade(),
                                dto.estado(),
                                dto.cep(),
                                dto.tipoCozinha(),
                                dto.horarioFuncionamento(),
                                null);

                Restaurante restauranteSalvo = cadastrarRestauranteUseCase.execute(novoRestaurante);

                RestauranteResponseDTO response = new RestauranteResponseDTO(
                                restauranteSalvo.getId(),
                                restauranteSalvo.getNome(),
                                restauranteSalvo.getEndereco().getEnderecoCompleto(),
                                restauranteSalvo.getTipoCozinha().getValor(),
                                restauranteSalvo.getHorarioFuncionamento().getValor());

                return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        @GetMapping
        public ResponseEntity<List<RestauranteResponseDTO>> listar() {
                List<RestauranteResponseDTO> restaurantes = restauranteGateway.listarTodos()
                                .stream()
                                .map(r -> new RestauranteResponseDTO(
                                                r.getId(),
                                                r.getNome(),
                                                r.getEndereco().getEnderecoCompleto(),
                                                r.getTipoCozinha().getValor(),
                                                r.getHorarioFuncionamento().getValor()))
                                .toList();
                return ResponseEntity.ok(restaurantes);
        }

        @GetMapping("/{id}")
        public ResponseEntity<RestauranteResponseDTO> obterPorId(@PathVariable Long id) {
                return restauranteGateway.obterPorId(id)
                                .map(r -> ResponseEntity.ok(new RestauranteResponseDTO(
                                                r.getId(),
                                                r.getNome(),
                                                r.getEndereco().getEnderecoCompleto(),
                                                r.getTipoCozinha().getValor(),
                                                r.getHorarioFuncionamento().getValor())))
                                .orElse(ResponseEntity.notFound().build());
        }

        @PutMapping("/{id}")
        public ResponseEntity<RestauranteResponseDTO> atualizar(@PathVariable Long id,
                        @Valid @RequestBody NovoRestauranteDTO dto) {
                return restauranteGateway.obterPorId(id)
                                .map(r -> {
                                        Restaurante atualizado = Restaurante.create(
                                                        id,
                                                        dto.nome(),
                                                        dto.logradouro(),
                                                        dto.numero(),
                                                        dto.complemento(),
                                                        dto.bairro(),
                                                        dto.cidade(),
                                                        dto.estado(),
                                                        dto.cep(),
                                                        dto.tipoCozinha(),
                                                        dto.horarioFuncionamento(),
                                                        null);

                                        Restaurante salvo = restauranteGateway.atualizar(atualizado);
                                        return ResponseEntity.ok(new RestauranteResponseDTO(
                                                        salvo.getId(),
                                                        salvo.getNome(),
                                                        salvo.getEndereco().getEnderecoCompleto(),
                                                        salvo.getTipoCozinha().getValor(),
                                                        salvo.getHorarioFuncionamento().getValor()));
                                })
                                .orElse(ResponseEntity.notFound().build());
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deletar(@PathVariable Long id) {
                if (restauranteGateway.obterPorId(id).isPresent()) {
                        restauranteGateway.deletar(id);
                        return ResponseEntity.noContent().build();
                }
                return ResponseEntity.notFound().build();
        }
}
