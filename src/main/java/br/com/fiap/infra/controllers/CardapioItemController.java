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

import jakarta.validation.Valid;
import br.com.fiap.core.domain.CardapioItem;
import br.com.fiap.core.gateways.ICardapioItemGateway;
import br.com.fiap.infra.dto.NovoItemCardapioDTO;
import br.com.fiap.infra.dto.CardapioItemResponseDTO;
import br.com.fiap.infra.mappers.CardapioItemMapper;

@RestController
@RequestMapping("/api/v1/cardapio")
public class CardapioItemController {

    private final ICardapioItemGateway cardapioItemGateway;

    public CardapioItemController(ICardapioItemGateway cardapioItemGateway) {
        this.cardapioItemGateway = cardapioItemGateway;
    }

    @PostMapping
    public ResponseEntity<CardapioItemResponseDTO> cadastrar(@Valid @RequestBody NovoItemCardapioDTO dto) {
        CardapioItem item = CardapioItemMapper.toDomain(dto);
        CardapioItem salvo = cardapioItemGateway.incluir(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(CardapioItemMapper.toResponse(salvo));
    }

    @GetMapping
    public ResponseEntity<List<CardapioItemResponseDTO>> listar() {
        List<CardapioItemResponseDTO> itens = cardapioItemGateway.listarTodos()
                .stream()
                .map(CardapioItemMapper::toResponse)
                .toList();
        return ResponseEntity.ok(itens);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardapioItemResponseDTO> obterPorId(@PathVariable Long id) {
        return cardapioItemGateway.obterPorId(id)
                .map(item -> ResponseEntity.ok(CardapioItemMapper.toResponse(item)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/restaurante/{restauranteId}")
    public ResponseEntity<List<CardapioItemResponseDTO>> listarPorRestaurante(@PathVariable Long restauranteId) {
        List<CardapioItemResponseDTO> itens = cardapioItemGateway.listarPorRestaurante(restauranteId)
                .stream()
                .map(CardapioItemMapper::toResponse)
                .toList();
        return ResponseEntity.ok(itens);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardapioItemResponseDTO> atualizar(@PathVariable Long id,
            @Valid @RequestBody NovoItemCardapioDTO dto) {
        return cardapioItemGateway.obterPorId(id)
                .map(item -> {
                    CardapioItem atualizado = CardapioItem.criar(id, dto.getRestauranteId(),
                            dto.getNome(), dto.getDescricao(), dto.getPreco(),
                            dto.isApenasPresencial(), dto.getCaminhoFoto());
                    CardapioItem salvo = cardapioItemGateway.atualizar(atualizado);
                    return ResponseEntity.ok(CardapioItemMapper.toResponse(salvo));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (cardapioItemGateway.obterPorId(id).isPresent()) {
            cardapioItemGateway.deletar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
