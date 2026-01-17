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
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/restaurantes")
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
    public ResponseEntity<RestauranteResponseDTO> cadastrar(@Valid @RequestBody NovoRestauranteDTO dto) {
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
    public ResponseEntity<RestauranteResponseDTO> obterPorId(@PathVariable Long id) {
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
    public ResponseEntity<RestauranteResponseDTO> atualizar(
            @PathVariable Long id,
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
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boolean deletado = deletarRestauranteUseCase.execute(id);
        if (deletado) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    
}
