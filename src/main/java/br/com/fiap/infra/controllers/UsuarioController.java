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
import br.com.fiap.core.usecases.usuario.ListarTodosUsuariosUseCase;
import br.com.fiap.core.usecases.usuario.TrocarSenhaUseCase;
import br.com.fiap.infra.dto.AtualizarUsuarioDTO;
import br.com.fiap.infra.dto.NovoUsuarioDTO;
import br.com.fiap.infra.dto.TrocarSenhaDTO;
import br.com.fiap.infra.dto.UsuarioResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários do sistema")
public class UsuarioController {
    
    private final CadastrarUsuarioUseCase cadastrarUsuarioUseCase;
    private final AtualizarUsuarioUseCase atualizarUsuarioUseCase;
    private final ExcluirUsuarioUseCase excluirUsuarioUseCase;
    private final TrocarSenhaUseCase trocarSenhaUseCase;
    private final BuscarUsuariosPorNomeUseCase buscarUsuariosPorNomeUseCase;
    private final ListarTodosUsuariosUseCase listarTodosUsuariosUseCase;
    
    public UsuarioController(
            CadastrarUsuarioUseCase cadastrarUsuarioUseCase,
            AtualizarUsuarioUseCase atualizarUsuarioUseCase,
            ExcluirUsuarioUseCase excluirUsuarioUseCase,
            TrocarSenhaUseCase trocarSenhaUseCase,
            BuscarUsuariosPorNomeUseCase buscarUsuariosPorNomeUseCase,
            ListarTodosUsuariosUseCase listarTodosUsuariosUseCase) {
        this.cadastrarUsuarioUseCase = cadastrarUsuarioUseCase;
        this.atualizarUsuarioUseCase = atualizarUsuarioUseCase;
        this.excluirUsuarioUseCase = excluirUsuarioUseCase;
        this.trocarSenhaUseCase = trocarSenhaUseCase;
        this.buscarUsuariosPorNomeUseCase = buscarUsuariosPorNomeUseCase;
        this.listarTodosUsuariosUseCase = listarTodosUsuariosUseCase;
    }
    
    @PostMapping
    @Operation(
        summary = "Cadastrar novo usuário",
        description = "**Este endpoint NÃO requer autenticação** - Pode ser usado para auto-registro.\n\n" +
                "Cria um novo usuário no sistema com dados pessoais, endereço e tipo de usuário. A senha deve conter pelo menos 8 caracteres, incluindo letras maiúsculas, minúsculas, números e caracteres especiais."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso",
            content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos, email já cadastrado, login já em uso ou senha não atende aos requisitos",
            content = @Content)
    })
    public ResponseEntity<UsuarioResponseDTO> cadastrar(
            @Parameter(description = "Dados do novo usuário", required = true)
            @Validated @RequestBody NovoUsuarioDTO dto) {
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
    @SecurityRequirement(name = "bearer-jwt")
    @Operation(
        summary = "Atualizar usuário",
        description = "**Requer autenticação JWT**\n\n" +
                "Atualiza dados pessoais e endereço de um usuário existente. Não é possível alterar a senha por este endpoint (use /trocar-senha)."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
            content = @Content),
        @ApiResponse(responseCode = "400", description = "Dados inválidos, email já cadastrado ou login já em uso",
            content = @Content),
        @ApiResponse(responseCode = "401", description = "Não autenticado",
            content = @Content)
    })
    public ResponseEntity<UsuarioResponseDTO> atualizar(
            @Parameter(description = "ID do usuário", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Dados atualizados do usuário", required = true)
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
    @SecurityRequirement(name = "bearer-jwt")
    @Operation(
        summary = "Excluir usuário",
        description = "**Requer autenticação JWT**\n\n" +
                "Remove permanentemente um usuário do sistema. Esta operação não pode ser desfeita."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<Void> excluir(
            @Parameter(description = "ID do usuário a ser excluído", required = true, example = "1")
            @PathVariable Long id) {
        excluirUsuarioUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/trocar-senha")
    @SecurityRequirement(name = "bearer-jwt")
    @Operation(
        summary = "Trocar senha do usuário",
        description = "**Requer autenticação JWT**\n\n" +
                "Permite alterar a senha de um usuário. É necessário informar a senha atual para confirmação. A nova senha deve conter pelo menos 8 caracteres, incluindo letras maiúsculas, minúsculas, números e caracteres especiais."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Senha alterada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "400", description = "Senha atual incorreta, nova senha inválida ou senhas não conferem"),
        @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<Void> trocarSenha(
            @Parameter(description = "ID do usuário", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Dados para troca de senha", required = true)
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
    @SecurityRequirement(name = "bearer-jwt")
    @Operation(
        summary = "Buscar usuários por nome",
        description = "**Requer autenticação JWT**\n\n" +
                "Retorna uma lista de usuários filtrados por nome (busca parcial, case-insensitive). Se nenhum nome for informado, retorna todos os usuários."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso"),
        @ApiResponse(responseCode = "401", description = "Não autenticado",
            content = @Content)
    })
    public ResponseEntity<List<UsuarioResponseDTO>> buscarPorNome(
            @Parameter(description = "Nome ou parte do nome do usuário (opcional)", example = "João")
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

    @GetMapping("/todos")
    @SecurityRequirement(name = "bearer-jwt")
    @Operation(
        summary = "Listar todos os usuários",
        description = "**Requer autenticação JWT**\n\n" +
                "Retorna a lista completa de todos os usuários cadastrados no sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso"),
        @ApiResponse(responseCode = "401", description = "Não autenticado",
            content = @Content)
    })
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos() {
        
        List<ListarTodosUsuariosUseCase.OutputModel> outputs = listarTodosUsuariosUseCase.execute();
        
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
