package br.com.fiap.infra.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

import br.com.fiap.core.usecases.usuario.AtualizarUsuarioUseCase;
import br.com.fiap.core.usecases.usuario.BuscarUsuariosPorNomeUseCase;
import br.com.fiap.core.usecases.usuario.CadastrarUsuarioUseCase;
import br.com.fiap.core.usecases.usuario.ExcluirUsuarioUseCase;
import br.com.fiap.core.usecases.usuario.TrocarSenhaUseCase;
import br.com.fiap.infra.dto.AtualizarUsuarioDTO;
import br.com.fiap.infra.dto.NovoUsuarioDTO;
import br.com.fiap.infra.dto.TrocarSenhaDTO;
import br.com.fiap.infra.dto.UsuarioResponseDTO;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    
    private final CadastrarUsuarioUseCase cadastrarUsuarioUseCase;
    private final AtualizarUsuarioUseCase atualizarUsuarioUseCase;
    private final ExcluirUsuarioUseCase excluirUsuarioUseCase;
    private final TrocarSenhaUseCase trocarSenhaUseCase;
    private final BuscarUsuariosPorNomeUseCase buscarUsuariosPorNomeUseCase;
    
    public UsuarioController(
            CadastrarUsuarioUseCase cadastrarUsuarioUseCase,
            AtualizarUsuarioUseCase atualizarUsuarioUseCase,
            ExcluirUsuarioUseCase excluirUsuarioUseCase,
            TrocarSenhaUseCase trocarSenhaUseCase,
            BuscarUsuariosPorNomeUseCase buscarUsuariosPorNomeUseCase) {
        this.cadastrarUsuarioUseCase = cadastrarUsuarioUseCase;
        this.atualizarUsuarioUseCase = atualizarUsuarioUseCase;
        this.excluirUsuarioUseCase = excluirUsuarioUseCase;
        this.trocarSenhaUseCase = trocarSenhaUseCase;
        this.buscarUsuariosPorNomeUseCase = buscarUsuariosPorNomeUseCase;
    }
    
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@Validated @RequestBody NovoUsuarioDTO dto) {
        var input = new CadastrarUsuarioUseCase.InputModel(
            dto.nome(),
            dto.email(),
            dto.login(),
            dto.senha(),
            dto.logradouro(),
            dto.numero(),
            dto.complemento(),
            dto.bairro(),
            dto.cidade(),
            dto.estado(),
            dto.cep(),
            dto.tipoUsuario()
        );

        var output = cadastrarUsuarioUseCase.execute(input);
        
        UsuarioResponseDTO response = new UsuarioResponseDTO(
            output.id(),
            output.nome(),
            output.email(),
            output.login(),
            output.endereco(),
            output.tipoUsuario()
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(
            @PathVariable Long id,
            @Validated @RequestBody AtualizarUsuarioDTO dto) {
        
        var input = new AtualizarUsuarioUseCase.InputModel(
            dto.nome(),
            dto.email(),
            dto.login(),
            dto.logradouro(),
            dto.numero(),
            dto.complemento(),
            dto.bairro(),
            dto.cidade(),
            dto.estado(),
            dto.cep()
        );
        
        var output = atualizarUsuarioUseCase.execute(id, input);
        
        UsuarioResponseDTO response = new UsuarioResponseDTO(
            output.id(),
            output.nome(),
            output.email(),
            output.login(),
            output.endereco(),
            output.tipoUsuario()
        );
        
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        excluirUsuarioUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/trocar-senha")
    public ResponseEntity<Void> trocarSenha(
            @PathVariable Long id,
            @Validated @RequestBody TrocarSenhaDTO dto) {
        
        var input = new TrocarSenhaUseCase.InputModel(
            dto.senhaAtual(),
            dto.novaSenha(),
            dto.confirmacaoSenha()
        );
        
        trocarSenhaUseCase.execute(id, input);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> buscarPorNome(
            @RequestParam(required = false, defaultValue = "") String nome) {
        
        List<BuscarUsuariosPorNomeUseCase.OutputModel> outputs = buscarUsuariosPorNomeUseCase.execute(nome);
        
        List<UsuarioResponseDTO> response = outputs.stream()
            .map(output -> new UsuarioResponseDTO(
                output.id(),
                output.nome(),
                output.email(),
                output.login(),
                output.endereco(),
                output.tipoUsuario()
            ))
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }
}
