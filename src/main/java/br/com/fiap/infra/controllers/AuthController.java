package br.com.fiap.infra.controllers;

import br.com.fiap.core.exceptions.SenhaAtualIncorretaException;
import br.com.fiap.core.exceptions.UsuarioNaoEncontradoException;
import br.com.fiap.core.usecases.usuario.AutenticarUsuarioUseCase;
import br.com.fiap.infra.dto.LoginRequestDTO;
import br.com.fiap.infra.dto.LoginResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AutenticarUsuarioUseCase autenticarUsuarioUseCase;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
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
