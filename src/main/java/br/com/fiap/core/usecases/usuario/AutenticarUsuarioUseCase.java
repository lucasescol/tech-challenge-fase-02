package br.com.fiap.core.usecases.usuario;

import br.com.fiap.core.domain.Usuario;
import br.com.fiap.core.exceptions.CampoObrigatorioException;
import br.com.fiap.core.exceptions.CredenciaisInvalidasException;
import br.com.fiap.core.gateways.IUsuarioGateway;
import br.com.fiap.core.services.IPasswordHasherService;
import br.com.fiap.core.services.ITokenService;

public class AutenticarUsuarioUseCase {

    public record InputModel(
        String login,
        String senha
    ) {}

    public record OutputModel(
        String token,
        String tipo,
        String login,
        String nome,
        String email,
        String tipoUsuario
    ) {}

    private final IUsuarioGateway usuarioGateway;
    private final IPasswordHasherService passwordHasherService;
    private final ITokenService tokenService;

    private AutenticarUsuarioUseCase(IUsuarioGateway usuarioGateway, 
                                     IPasswordHasherService passwordHasherService,
                                     ITokenService tokenService) {
        this.usuarioGateway = usuarioGateway;
        this.passwordHasherService = passwordHasherService;
        this.tokenService = tokenService;
    }

    public static AutenticarUsuarioUseCase create(IUsuarioGateway usuarioGateway, 
                                                   IPasswordHasherService passwordHasherService,
                                                   ITokenService tokenService) {
        return new AutenticarUsuarioUseCase(usuarioGateway, passwordHasherService, tokenService);
    }

    public OutputModel execute(InputModel input) {
        if (input.login() == null || input.login().trim().isEmpty()) {
            throw new CampoObrigatorioException("Login");
        }
        
        if (input.senha() == null || input.senha().trim().isEmpty()) {
            throw new CampoObrigatorioException("Senha");
        }
        
        Usuario usuario = usuarioGateway.buscarPorLogin(input.login())
            .orElseThrow(CredenciaisInvalidasException::new);

        if (!passwordHasherService.matches(input.senha(), usuario.getSenha())) {
            throw new CredenciaisInvalidasException();
        }

        String token = tokenService.generateToken(usuario.getLogin(), usuario.getTipoUsuario().getNome());

        return new OutputModel(
            token,
            "Bearer",
            usuario.getLogin(),
            usuario.getNome(),
            usuario.getEmail().getValor(),
            usuario.getTipoUsuario().getNome()
        );
    }
}
