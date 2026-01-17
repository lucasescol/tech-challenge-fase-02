package br.com.fiap.infra.controllers;

import java.util.List;
import java.util.stream.Collectors;

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

import br.com.fiap.core.usecases.restaurante.AtualizarRestauranteUseCase;
import br.com.fiap.core.usecases.restaurante.CadastrarRestauranteUseCase;
import br.com.fiap.core.usecases.restaurante.DeletarRestauranteUseCase;
import br.com.fiap.core.usecases.restaurante.ListarTodosRestaurantesUseCase;
import br.com.fiap.core.usecases.restaurante.ObterRestaurantePorIdUseCase;
import br.com.fiap.infra.dto.AtualizarRestauranteDTO;
import br.com.fiap.infra.dto.NovoRestauranteDTO;
import br.com.fiap.infra.dto.RestauranteResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/restaurantes")
@Tag(name = "Restaurantes", description = "Endpoints para gerenciamento de restaurantes")
@SecurityRequirement(name = "bearer-jwt")
public class RestauranteController {
    
    private final CadastrarRestauranteUseCase cadastrarRestauranteUseCase;
    private final ObterRestaurantePorIdUseCase obterRestaurantePorIdUseCase;
    private final ListarTodosRestaurantesUseCase listarTodosRestaurantesUseCase;
    private final AtualizarRestauranteUseCase atualizarRestauranteUseCase;
    private final DeletarRestauranteUseCase deletarRestauranteUseCase;
    
    public RestauranteController(
            CadastrarRestauranteUseCase cadastrarRestauranteUseCase,
            ObterRestaurantePorIdUseCase obterRestaurantePorIdUseCase,
            ListarTodosRestaurantesUseCase listarTodosRestaurantesUseCase,
            AtualizarRestauranteUseCase atualizarRestauranteUseCase,
            DeletarRestauranteUseCase deletarRestauranteUseCase) {
        this.cadastrarRestauranteUseCase = cadastrarRestauranteUseCase;
        this.obterRestaurantePorIdUseCase = obterRestaurantePorIdUseCase;
        this.listarTodosRestaurantesUseCase = listarTodosRestaurantesUseCase;
        this.atualizarRestauranteUseCase = atualizarRestauranteUseCase;
        this.deletarRestauranteUseCase = deletarRestauranteUseCase;
    }
    
    @PostMapping
    @Operation(
        summary = "Cadastrar novo restaurante",
        description = "Cria um novo restaurante no sistema com informações de endereço, tipo de cozinha e horário de funcionamento"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Restaurante cadastrado com sucesso",
            content = @Content(schema = @Schema(implementation = RestauranteResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou tipo de cozinha não permitido",
            content = @Content),
        @ApiResponse(responseCode = "401", description = "Não autenticado",
            content = @Content)
    })
    public ResponseEntity<RestauranteResponseDTO> cadastrar(
            @Parameter(description = "Dados do novo restaurante", required = true)
            @Valid @RequestBody NovoRestauranteDTO dto) {
        var input = new CadastrarRestauranteUseCase.InputModel(
            dto.nome(),
            dto.logradouro(),
            dto.numero(),
            dto.complemento(),
            dto.bairro(),
            dto.cidade(),
            dto.estado(),
            dto.cep(),
            dto.tipoCozinha(),
            dto.horarioFuncionamento()
        );

        var output = cadastrarRestauranteUseCase.execute(input);
        
        RestauranteResponseDTO response = new RestauranteResponseDTO(
            output.id(),
            output.nome(),
            output.endereco(),
            output.tipoCozinha(),
            output.horarioFuncionamento()
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar restaurante por ID",
        description = "Retorna os detalhes completos de um restaurante específico pelo seu identificador"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Restaurante encontrado",
            content = @Content(schema = @Schema(implementation = RestauranteResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado",
            content = @Content),
        @ApiResponse(responseCode = "401", description = "Não autenticado",
            content = @Content)
    })
    public ResponseEntity<RestauranteResponseDTO> obterPorId(
            @Parameter(description = "ID do restaurante", required = true, example = "1")
            @PathVariable Long id) {
        return obterRestaurantePorIdUseCase.execute(id)
                .map(output -> new RestauranteResponseDTO(
                        output.id(),
                        output.nome(),
                        output.endereco(),
                        output.tipoCozinha(),
                        output.horarioFuncionamento()
                ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(
        summary = "Listar todos os restaurantes",
        description = "Retorna uma lista completa com todos os restaurantes cadastrados no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de restaurantes retornada com sucesso"),
        @ApiResponse(responseCode = "401", description = "Não autenticado",
            content = @Content)
    })
    public ResponseEntity<List<RestauranteResponseDTO>> listarTodos() {
        List<RestauranteResponseDTO> response = listarTodosRestaurantesUseCase.execute()
                .stream()
                .map(output -> new RestauranteResponseDTO(
                        output.id(),
                        output.nome(),
                        output.endereco(),
                        output.tipoCozinha(),
                        output.horarioFuncionamento()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar restaurante",
        description = "Atualiza todas as informações de um restaurante existente (nome, endereço, tipo de cozinha, horário)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Restaurante atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = RestauranteResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado",
            content = @Content),
        @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content),
        @ApiResponse(responseCode = "401", description = "Não autenticado",
            content = @Content)
    })
    public ResponseEntity<RestauranteResponseDTO> atualizar(
            @Parameter(description = "ID do restaurante", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Dados atualizados do restaurante", required = true)
            @Valid @RequestBody AtualizarRestauranteDTO dto) {

        var input = new AtualizarRestauranteUseCase.InputModel(
            dto.nome(),
            dto.logradouro(),
            dto.numero(),
            dto.complemento(),
            dto.bairro(),
            dto.cidade(),
            dto.estado(),
            dto.cep(),
            dto.tipoCozinha(),
            dto.horarioFuncionamento()
        );

        return atualizarRestauranteUseCase.execute(id, input)
                .map(output -> {
                    RestauranteResponseDTO response = new RestauranteResponseDTO(
                            output.id(),
                            output.nome(),
                            output.endereco(),
                            output.tipoCozinha(),
                            output.horarioFuncionamento()
                    );
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Deletar restaurante",
        description = "Remove permanentemente um restaurante do sistema. Esta operação não pode ser desfeita."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Restaurante deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do restaurante a ser deletado", required = true, example = "1")
            @PathVariable Long id) {
        boolean deletado = deletarRestauranteUseCase.execute(id);
        if (deletado) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    
}
