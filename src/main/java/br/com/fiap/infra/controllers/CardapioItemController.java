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

import br.com.fiap.core.domain.CardapioItem;
import br.com.fiap.core.usecases.cardapio_item.AtualizarCardapioItemUseCase;
import br.com.fiap.core.usecases.cardapio_item.CadastrarCardapioItemUseCase;
import br.com.fiap.core.usecases.cardapio_item.DeletarCardapioItemUseCase;
import br.com.fiap.core.usecases.cardapio_item.ListarCardapioItensPorRestauranteUseCase;
import br.com.fiap.core.usecases.cardapio_item.ListarTodosCardapioItensUseCase;
import br.com.fiap.core.usecases.cardapio_item.ObterCardapioItemPorIdUseCase;
import br.com.fiap.infra.dto.AtualizarCardapioItemDTO;
import br.com.fiap.infra.dto.CardapioItemResponseDTO;
import br.com.fiap.infra.dto.NovoCardapioItemDTO;
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
@RequestMapping("/api/cardapio-itens")
@Tag(name = "Cardápio Itens", description = "Endpoints para gerenciamento de itens do cardápio")
@SecurityRequirement(name = "bearer-jwt")
public class CardapioItemController {

    private final CadastrarCardapioItemUseCase cadastrarCardapioItemUseCase;
    private final ObterCardapioItemPorIdUseCase obterCardapioItemPorIdUseCase;
    private final ListarCardapioItensPorRestauranteUseCase listarCardapioItensPorRestauranteUseCase;
    private final ListarTodosCardapioItensUseCase listarTodosCardapioItensUseCase;
    private final AtualizarCardapioItemUseCase atualizarCardapioItemUseCase;
    private final DeletarCardapioItemUseCase deletarCardapioItemUseCase;

    public CardapioItemController(
            CadastrarCardapioItemUseCase cadastrarCardapioItemUseCase,
            ObterCardapioItemPorIdUseCase obterCardapioItemPorIdUseCase,
            ListarCardapioItensPorRestauranteUseCase listarCardapioItensPorRestauranteUseCase,
            ListarTodosCardapioItensUseCase listarTodosCardapioItensUseCase,
            AtualizarCardapioItemUseCase atualizarCardapioItemUseCase,
            DeletarCardapioItemUseCase deletarCardapioItemUseCase) {
        this.cadastrarCardapioItemUseCase = cadastrarCardapioItemUseCase;
        this.obterCardapioItemPorIdUseCase = obterCardapioItemPorIdUseCase;
        this.listarCardapioItensPorRestauranteUseCase = listarCardapioItensPorRestauranteUseCase;
        this.listarTodosCardapioItensUseCase = listarTodosCardapioItensUseCase;
        this.atualizarCardapioItemUseCase = atualizarCardapioItemUseCase;
        this.deletarCardapioItemUseCase = deletarCardapioItemUseCase;
    }

    @PostMapping
    @Operation(
        summary = "Cadastrar novo item do cardápio",
        description = "Cria um novo item no cardápio de um restaurante. Apenas o dono do restaurante ou administradores podem realizar esta operação."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Item cadastrado com sucesso",
            content = @Content(schema = @Schema(implementation = CardapioItemResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content),
        @ApiResponse(responseCode = "401", description = "Não autenticado",
            content = @Content),
        @ApiResponse(responseCode = "403", description = "Sem permissão para cadastrar item neste restaurante",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado",
            content = @Content)
    })
    public ResponseEntity<CardapioItemResponseDTO> cadastrar(
            @Parameter(description = "Dados do novo item do cardápio", required = true)
            @Valid @RequestBody NovoCardapioItemDTO dto) {
        
        CardapioItem novoItem = CardapioItem.criar(
            null,
            dto.restauranteId(),
            dto.nome(),
            dto.descricao(),
            dto.preco(),
            dto.apenasPresencial(),
            dto.caminhoFoto()
        );

        CardapioItem itemSalvo = cadastrarCardapioItemUseCase.execute(novoItem);
        
        CardapioItemResponseDTO response = new CardapioItemResponseDTO(
            itemSalvo.getId(),
            itemSalvo.getRestauranteId(),
            itemSalvo.getNome(),
            itemSalvo.getDescricao(),
            itemSalvo.getPreco(),
            itemSalvo.isApenasPresencial(),
            itemSalvo.getCaminhoFoto()
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar item do cardápio por ID",
        description = "Retorna os detalhes completos de um item do cardápio específico pelo seu identificador"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item encontrado",
            content = @Content(schema = @Schema(implementation = CardapioItemResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Item não encontrado",
            content = @Content),
        @ApiResponse(responseCode = "401", description = "Não autenticado",
            content = @Content)
    })
    public ResponseEntity<CardapioItemResponseDTO> obterPorId(
            @Parameter(description = "ID do item do cardápio", required = true, example = "1")
            @PathVariable Long id) {
        return obterCardapioItemPorIdUseCase.execute(id)
                .map(output -> new CardapioItemResponseDTO(
                        output.id(),
                        output.restauranteId(),
                        output.nome(),
                        output.descricao(),
                        output.preco(),
                        output.apenasPresencial(),
                        output.caminhoFoto()
                ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/restaurante/{restauranteId}")
    @Operation(
        summary = "Listar itens do cardápio por restaurante",
        description = "Retorna uma lista com todos os itens do cardápio de um restaurante específico"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de itens retornada com sucesso"),
        @ApiResponse(responseCode = "401", description = "Não autenticado",
            content = @Content)
    })
    public ResponseEntity<List<CardapioItemResponseDTO>> listarPorRestaurante(
            @Parameter(description = "ID do restaurante", required = true, example = "1")
            @PathVariable Long restauranteId) {
        List<CardapioItemResponseDTO> response = listarCardapioItensPorRestauranteUseCase.execute(restauranteId)
                .stream()
                .map(output -> new CardapioItemResponseDTO(
                        output.id(),
                        output.restauranteId(),
                        output.nome(),
                        output.descricao(),
                        output.preco(),
                        output.apenasPresencial(),
                        output.caminhoFoto()
                ))
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(
        summary = "Listar todos os itens do cardápio",
        description = "Retorna uma lista completa com todos os itens do cardápio de todos os restaurantes"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de itens retornada com sucesso"),
        @ApiResponse(responseCode = "401", description = "Não autenticado",
            content = @Content)
    })
    public ResponseEntity<List<CardapioItemResponseDTO>> listarTodos() {
        List<CardapioItemResponseDTO> response = listarTodosCardapioItensUseCase.execute()
                .stream()
                .map(output -> new CardapioItemResponseDTO(
                        output.id(),
                        output.restauranteId(),
                        output.nome(),
                        output.descricao(),
                        output.preco(),
                        output.apenasPresencial(),
                        output.caminhoFoto()
                ))
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar item do cardápio",
        description = "Atualiza os dados de um item do cardápio existente. Apenas o dono do restaurante ou administradores podem realizar esta operação."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = CardapioItemResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content),
        @ApiResponse(responseCode = "401", description = "Não autenticado",
            content = @Content),
        @ApiResponse(responseCode = "403", description = "Sem permissão para atualizar este item",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Item não encontrado",
            content = @Content)
    })
    public ResponseEntity<CardapioItemResponseDTO> atualizar(
            @Parameter(description = "ID do item do cardápio", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Dados atualizados do item", required = true)
            @Valid @RequestBody AtualizarCardapioItemDTO dto) {
        
        var input = new AtualizarCardapioItemUseCase.InputModel(
            dto.nome(),
            dto.descricao(),
            dto.preco(),
            dto.apenasPresencial(),
            dto.caminhoFoto()
        );

        return atualizarCardapioItemUseCase.execute(id, input)
                .map(output -> new CardapioItemResponseDTO(
                        output.id(),
                        output.restauranteId(),
                        output.nome(),
                        output.descricao(),
                        output.preco(),
                        output.apenasPresencial(),
                        output.caminhoFoto()
                ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Deletar item do cardápio",
        description = "Remove um item do cardápio. Apenas o dono do restaurante ou administradores podem realizar esta operação."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Item deletado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Não autenticado",
            content = @Content),
        @ApiResponse(responseCode = "403", description = "Sem permissão para deletar este item",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Item não encontrado",
            content = @Content)
    })
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do item do cardápio", required = true, example = "1")
            @PathVariable Long id) {
        deletarCardapioItemUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
