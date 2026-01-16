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
import br.com.fiap.core.domain.TipoUsuario;
import br.com.fiap.core.gateways.ITipoUsuarioGateway;
import br.com.fiap.infra.dto.NovoTipoUsuarioDTO;
import br.com.fiap.infra.dto.TipoUsuarioResponseDTO;
import br.com.fiap.infra.mappers.TipoUsuarioMapper;

@RestController
@RequestMapping("/api/v1/tipos-usuario")
public class TipoUsuarioController {

    private final ITipoUsuarioGateway tipoUsuarioGateway;

    public TipoUsuarioController(ITipoUsuarioGateway tipoUsuarioGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
    }

    @PostMapping
    public ResponseEntity<TipoUsuarioResponseDTO> cadastrar(@Valid @RequestBody NovoTipoUsuarioDTO dto) {
        TipoUsuario tipoUsuario = TipoUsuarioMapper.toDomain(dto);
        TipoUsuario salvo = tipoUsuarioGateway.incluir(tipoUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(TipoUsuarioMapper.toResponse(salvo));
    }

    @GetMapping
    public ResponseEntity<List<TipoUsuarioResponseDTO>> listar() {
        List<TipoUsuarioResponseDTO> tipos = tipoUsuarioGateway.listarTodos()
                .stream()
                .map(TipoUsuarioMapper::toResponse)
                .toList();
        return ResponseEntity.ok(tipos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoUsuarioResponseDTO> obterPorId(@PathVariable Long id) {
        return tipoUsuarioGateway.obterPorId(id)
                .map(tipo -> ResponseEntity.ok(TipoUsuarioMapper.toResponse(tipo)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoUsuarioResponseDTO> atualizar(@PathVariable Long id,
            @Valid @RequestBody NovoTipoUsuarioDTO dto) {
        return tipoUsuarioGateway.obterPorId(id)
                .map(tipo -> {
                    TipoUsuario atualizado = TipoUsuario.criar(id, dto.getNome(),
                            TipoUsuario.TipoConta.valueOf(dto.getTipo()), dto.getDescricao());
                    TipoUsuario salvo = tipoUsuarioGateway.atualizar(atualizado);
                    return ResponseEntity.ok(TipoUsuarioMapper.toResponse(salvo));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (tipoUsuarioGateway.obterPorId(id).isPresent()) {
            tipoUsuarioGateway.deletar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
