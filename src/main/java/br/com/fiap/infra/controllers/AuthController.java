package br.com.fiap.infra.controllers;

import br.com.fiap.core.exceptions.SenhaAtualIncorretaException;
import br.com.fiap.core.exceptions.UsuarioNaoEncontradoException;
import br.com.fiap.core.usecases.usuario.AutenticarUsuarioUseCase;
import br.com.fiap.infra.dto.LoginRequestDTO;
import br.com.fiap.infra.dto.LoginResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoint para autenticação de usuários e geração de tokens JWT")
public class AuthController {

    @Autowired
    private AutenticarUsuarioUseCase autenticarUsuarioUseCase;

    @PostMapping("/login")
    @Operation(
        summary = "Realizar login",
        description = "Autentica um usuário com login e senha, retornando um token JWT válido por 24 horas. Este token deve ser usado no header Authorization (Bearer token) para acessar endpoints protegidos."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
            content = @Content(schema = @Schema(implementation = LoginResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas (login ou senha incorretos)",
            content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
            content = @Content)
    })
    public ResponseEntity<?> login(
            @Parameter(description = "Credenciais de login", required = true)
            @Valid @RequestBody LoginRequestDTO loginRequest) {
        try {
            AutenticarUsuarioUseCase.InputModel input = new AutenticarUsuarioUseCase.InputModel(
                loginRequest.login(),
                loginRequest.senha()
            );
            
            AutenticarUsuarioUseCase.OutputModel resultado = autenticarUsuarioUseCase.execute(input);

            LoginResponseDTO response = new LoginResponseDTO(
                resultado.token(),
                resultado.tipo(),
                resultado.login(),
                resultado.tipoUsuario()
            );

            return ResponseEntity.ok(response);
            
        } catch (UsuarioNaoEncontradoException | SenhaAtualIncorretaException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Credenciais inválidas");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao processar autenticação: " + e.getMessage());
        }
    }
}
