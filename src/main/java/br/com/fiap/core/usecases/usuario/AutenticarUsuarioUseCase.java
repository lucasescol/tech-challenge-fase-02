package br.com.fiap.core.usecases.usuario;

import br.com.fiap.core.domain.Usuario;
import br.com.fiap.core.exceptions.CredenciaisInvalidasException;
import br.com.fiap.core.gateways.IAuthenticationGateway;
import br.com.fiap.core.services.IPasswordHasherService;
import br.com.fiap.core.services.ITokenService;
import br.com.fiap.infra.dto.LoginRequestDTO;
import br.com.fiap.infra.dto.LoginResponseDTO;

public class AutenticarUsuarioUseCase {

    private final IAuthenticationGateway authenticationGateway;
    private final IPasswordHasherService passwordHasherService;
    private final ITokenService tokenService;

    public AutenticarUsuarioUseCase(
            IAuthenticationGateway authenticationGateway,
            IPasswordHasherService passwordHasherService,
            ITokenService tokenService) {
        this.authenticationGateway = authenticationGateway;
        this.passwordHasherService = passwordHasherService;
        this.tokenService = tokenService;
    }

    public LoginResponseDTO executar(LoginRequestDTO input) {

        Usuario usuario = authenticationGateway.buscarPorEmail(input.email())
                .orElseThrow(() -> new CredenciaisInvalidasException("Email ou senha incorretos"));

        if (!passwordHasherService.verify(input.senha(), usuario.getSenha())) {
            throw new CredenciaisInvalidasException("Email ou senha incorretos");
        }

        String token = tokenService.gerarToken(
                usuario.getId(),
                usuario.getEmail().getValor(),
                usuario.getTipoUsuario().toString());

        return new LoginResponseDTO(
                token,
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail().getValor(),
                usuario.getTipoUsuario().toString());
    }
}
