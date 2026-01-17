package br.com.fiap.infra.controllers;

import br.com.fiap.core.usecases.tipo_usuario.AtualizarTipoUsuarioUseCase;
import br.com.fiap.core.usecases.tipo_usuario.CadastrarTipoUsuarioUseCase;
import br.com.fiap.core.usecases.tipo_usuario.DeletarTipoUsuarioUseCase;
import br.com.fiap.core.usecases.tipo_usuario.ListarTodosTiposUsuarioUseCase;
import br.com.fiap.core.usecases.tipo_usuario.ObterTipoUsuarioPorIdUseCase;
import br.com.fiap.infra.dto.AtualizarTipoUsuarioDTO;
import br.com.fiap.infra.dto.NovoTipoUsuarioDTO;
import br.com.fiap.infra.dto.TipoUsuarioResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tipos-usuario")
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
    public ResponseEntity<TipoUsuarioResponseDTO> cadastrar(@Valid @RequestBody NovoTipoUsuarioDTO dto) {
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
    public ResponseEntity<TipoUsuarioResponseDTO> obterPorId(@PathVariable Long id) {
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
    public ResponseEntity<TipoUsuarioResponseDTO> atualizar(
            @PathVariable Long id,
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
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boolean deletado = deletarTipoUsuarioUseCase.execute(id);
        if (deletado) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
