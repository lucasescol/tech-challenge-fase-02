package br.com.fiap.infra.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import br.com.fiap.core.domain.Usuario;
import br.com.fiap.core.domain.Endereco;
import br.com.fiap.core.gateways.IUsuarioGateway;
import br.com.fiap.core.usecases.usuario.TrocarSenhaUseCase;
import br.com.fiap.core.usecases.usuario.AtualizarUsuarioUseCase;
import br.com.fiap.core.usecases.usuario.BuscarUsuariosPorNomeUseCase;
import br.com.fiap.infra.dto.NovoUsuarioDTO;
import br.com.fiap.infra.dto.UsuarioResponseDTO;
import br.com.fiap.infra.dto.TrocarSenhaDTO;
import br.com.fiap.infra.dto.AtualizarUsuarioDTO;
import br.com.fiap.infra.mappers.UsuarioMapper;
import br.com.fiap.infra.mappers.EnderecoMapper;

@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários")
public class UsuarioController {

    private final IUsuarioGateway usuarioGateway;
    private final TrocarSenhaUseCase trocarSenhaUseCase;
    private final AtualizarUsuarioUseCase atualizarUsuarioUseCase;
    private final BuscarUsuariosPorNomeUseCase buscarUsuariosPorNomeUseCase;

    public UsuarioController(
            IUsuarioGateway usuarioGateway,
            TrocarSenhaUseCase trocarSenhaUseCase,
            AtualizarUsuarioUseCase atualizarUsuarioUseCase,
            BuscarUsuariosPorNomeUseCase buscarUsuariosPorNomeUseCase) {
        this.usuarioGateway = usuarioGateway;
        this.trocarSenhaUseCase = trocarSenhaUseCase;
        this.atualizarUsuarioUseCase = atualizarUsuarioUseCase;
        this.buscarUsuariosPorNomeUseCase = buscarUsuariosPorNomeUseCase;
    }

    @PostMapping
    @Operation(summary = "Criar novo usuário", description = "Cria um novo usuário com os dados fornecidos")
    @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@Valid @RequestBody NovoUsuarioDTO dto) {
        Usuario usuario = UsuarioMapper.toDomain(dto);
        Usuario salvo = usuarioGateway.incluir(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toResponse(salvo));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        List<UsuarioResponseDTO> usuarios = usuarioGateway.listarTodos()
                .stream()
                .map(UsuarioMapper::toResponse)
                .toList();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obterPorId(@PathVariable Long id) {
        return usuarioGateway.obterPorId(id)
                .map(usuario -> ResponseEntity.ok(UsuarioMapper.toResponse(usuario)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioResponseDTO> obterPorEmail(@PathVariable String email) {
        return usuarioGateway.obterPorEmail(email)
                .map(usuario -> ResponseEntity.ok(UsuarioMapper.toResponse(usuario)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<UsuarioResponseDTO>> buscarPorNome(@RequestParam String nome) {
        List<UsuarioResponseDTO> usuarios = buscarUsuariosPorNomeUseCase.executar(nome)
                .stream()
                .map(UsuarioMapper::toResponse)
                .toList();
        return ResponseEntity.ok(usuarios);
    }

    @PutMapping("/{id}/perfil")
    public ResponseEntity<UsuarioResponseDTO> atualizarPerfil(@PathVariable Long id,
            @Valid @RequestBody AtualizarUsuarioDTO dto) {
        try {
            Endereco endereco = dto.getEndereco() != null ? EnderecoMapper.toDomain(dto.getEndereco()) : null;

            AtualizarUsuarioUseCase.InputModel input = new AtualizarUsuarioUseCase.InputModel(
                    dto.getNome(),
                    dto.getEmail(),
                    dto.getLogin(),
                    endereco);

            Usuario usuarioAtualizado = atualizarUsuarioUseCase.executar(id, input);
            return ResponseEntity.ok(UsuarioMapper.toResponse(usuarioAtualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/senha")
    public ResponseEntity<Void> trocarSenha(@PathVariable Long id,
            @Valid @RequestBody TrocarSenhaDTO dto) {
        try {
            trocarSenhaUseCase.executar(id, new TrocarSenhaUseCase.InputModel(
                    dto.getSenhaAtual(),
                    dto.getNovaSenha(),
                    dto.getConfirmacaoSenha()));
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(@PathVariable Long id,
            @Valid @RequestBody NovoUsuarioDTO dto) {
        return usuarioGateway.obterPorId(id)
                .map(usuario -> {
                    Usuario.TipoUsuario tipo = Usuario.TipoUsuario.valueOf(dto.getTipoUsuario());
                    Endereco endereco = dto.getEndereco() != null ? EnderecoMapper.toDomain(dto.getEndereco()) : null;
                    Usuario usuarioAtualizado = Usuario.criar(id, dto.getNome(), dto.getEmail(),
                            dto.getLogin(), dto.getSenha(), endereco, tipo);
                    Usuario salvo = usuarioGateway.atualizar(usuarioAtualizado);
                    return ResponseEntity.ok(UsuarioMapper.toResponse(salvo));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (usuarioGateway.obterPorId(id).isPresent()) {
            usuarioGateway.deletar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
