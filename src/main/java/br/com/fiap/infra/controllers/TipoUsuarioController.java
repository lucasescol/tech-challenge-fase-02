package br.com.fiap.infra.controllers;

import br.com.fiap.core.usecases.tipo_usuario.AtualizarTipoUsuarioUseCase;
import br.com.fiap.core.usecases.tipo_usuario.CadastrarTipoUsuarioUseCase;
import br.com.fiap.core.usecases.tipo_usuario.DeletarTipoUsuarioUseCase;
import br.com.fiap.core.usecases.tipo_usuario.ListarTodosTiposUsuarioUseCase;
import br.com.fiap.core.usecases.tipo_usuario.ObterTipoUsuarioPorIdUseCase;
import br.com.fiap.infra.dto.AtualizarTipoUsuarioDTO;
import br.com.fiap.infra.dto.NovoTipoUsuarioDTO;
import br.com.fiap.infra.dto.TipoUsuarioResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tipos-usuario")
@Tag(name = "Tipos de Usuário", description = "Endpoints para gerenciamento de tipos de usuário (CLIENTE, ADMIN, etc.)")
@SecurityRequirement(name = "bearer-jwt")
public class TipoUsuarioController {

    private final CadastrarTipoUsuarioUseCase cadastrarTipoUsuarioUseCase;
    private final ObterTipoUsuarioPorIdUseCase obterTipoUsuarioPorIdUseCase;
    private final ListarTodosTiposUsuarioUseCase listarTodosTiposUsuarioUseCase;
    private final AtualizarTipoUsuarioUseCase atualizarTipoUsuarioUseCase;
    private final DeletarTipoUsuarioUseCase deletarTipoUsuarioUseCase;

    public TipoUsuarioController(
            CadastrarTipoUsuarioUseCase cadastrarTipoUsuarioUseCase,
            ObterTipoUsuarioPorIdUseCase obterTipoUsuarioPorIdUseCase,
            ListarTodosTiposUsuarioUseCase listarTodosTiposUsuarioUseCase,
            AtualizarTipoUsuarioUseCase atualizarTipoUsuarioUseCase,
            DeletarTipoUsuarioUseCase deletarTipoUsuarioUseCase) {
        this.cadastrarTipoUsuarioUseCase = cadastrarTipoUsuarioUseCase;
        this.obterTipoUsuarioPorIdUseCase = obterTipoUsuarioPorIdUseCase;
        this.listarTodosTiposUsuarioUseCase = listarTodosTiposUsuarioUseCase;
        this.atualizarTipoUsuarioUseCase = atualizarTipoUsuarioUseCase;
        this.deletarTipoUsuarioUseCase = deletarTipoUsuarioUseCase;
    }

    @PostMapping
    @Operation(
        summary = "Cadastrar novo tipo de usuário",
        description = "Cria um novo tipo de usuário no sistema (ex: CLIENTE, ADMIN, FUNCIONÁRIO, GERENTE)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tipo de usuário cadastrado com sucesso",
            content = @Content(schema = @Schema(implementation = TipoUsuarioResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content),
        @ApiResponse(responseCode = "401", description = "Não autenticado",
            content = @Content)
    })
    public ResponseEntity<TipoUsuarioResponseDTO> cadastrar(
            @Parameter(description = "Dados do novo tipo de usuário", required = true)
            @Valid @RequestBody NovoTipoUsuarioDTO dto) {
        var input = new CadastrarTipoUsuarioUseCase.InputModel(dto.nome(), dto.descricao());
        var output = cadastrarTipoUsuarioUseCase.execute(input);

        TipoUsuarioResponseDTO response = new TipoUsuarioResponseDTO(
                output.id(),
                output.nome(),
                output.descricao()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar tipo de usuário por ID",
        description = "Retorna os detalhes de um tipo de usuário específico pelo seu identificador"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tipo de usuário encontrado",
            content = @Content(schema = @Schema(implementation = TipoUsuarioResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Tipo de usuário não encontrado",
            content = @Content),
        @ApiResponse(responseCode = "401", description = "Não autenticado",
            content = @Content)
    })
    public ResponseEntity<TipoUsuarioResponseDTO> obterPorId(
            @Parameter(description = "ID do tipo de usuário", required = true, example = "1")
            @PathVariable Long id) {
        return obterTipoUsuarioPorIdUseCase.execute(id)
                .map(output -> new TipoUsuarioResponseDTO(
                        output.id(),
                        output.nome(),
                        output.descricao()
                ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(
        summary = "Listar todos os tipos de usuário",
        description = "Retorna uma lista completa com todos os tipos de usuário cadastrados no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tipos de usuário retornada com sucesso"),
        @ApiResponse(responseCode = "401", description = "Não autenticado",
            content = @Content)
    })
    public ResponseEntity<List<TipoUsuarioResponseDTO>> listarTodos() {
        List<TipoUsuarioResponseDTO> response = listarTodosTiposUsuarioUseCase.execute()
                .stream()
                .map(output -> new TipoUsuarioResponseDTO(
                        output.id(),
                        output.nome(),
                        output.descricao()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar tipo de usuário",
        description = "Atualiza o nome e descrição de um tipo de usuário existente"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tipo de usuário atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = TipoUsuarioResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Tipo de usuário não encontrado",
            content = @Content),
        @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content),
        @ApiResponse(responseCode = "401", description = "Não autenticado",
            content = @Content)
    })
    public ResponseEntity<TipoUsuarioResponseDTO> atualizar(
            @Parameter(description = "ID do tipo de usuário", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Dados atualizados do tipo de usuário", required = true)
            @Valid @RequestBody AtualizarTipoUsuarioDTO dto) {

        var input = new AtualizarTipoUsuarioUseCase.InputModel(dto.nome(), dto.descricao());

        return atualizarTipoUsuarioUseCase.execute(id, input)
                .map(output -> {
                    TipoUsuarioResponseDTO response = new TipoUsuarioResponseDTO(
                            output.id(),
                            output.nome(),
                            output.descricao()
                    );
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Deletar tipo de usuário",
        description = "Remove permanentemente um tipo de usuário do sistema. Esta operação não pode ser desfeita."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Tipo de usuário deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Tipo de usuário não encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do tipo de usuário a ser deletado", required = true, example = "1")
            @PathVariable Long id) {
        boolean deletado = deletarTipoUsuarioUseCase.execute(id);
        if (deletado) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
