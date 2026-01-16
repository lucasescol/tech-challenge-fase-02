package br.com.fiap.core.usecases.usuario;

import br.com.fiap.core.exceptions.SenhaInvalidaException;
import br.com.fiap.core.exceptions.SenhasNaoConferemException;
import br.com.fiap.core.gateways.IAuthenticationGateway;
import br.com.fiap.core.services.IPasswordHasherService;

public class TrocarSenhaUseCase {

    public record InputModel(
            String senhaAtual,
            String novaSenha,
            String confirmacaoSenha) {
    }

    private final IAuthenticationGateway authenticationGateway;
    private final IPasswordHasherService passwordHasherService;

    public TrocarSenhaUseCase(
            IAuthenticationGateway authenticationGateway,
            IPasswordHasherService passwordHasherService) {
        this.authenticationGateway = authenticationGateway;
        this.passwordHasherService = passwordHasherService;
    }

    public void executar(Long idUsuario, InputModel input) {
        if (!input.novaSenha().equals(input.confirmacaoSenha())) {
            throw new SenhasNaoConferemException();
        }

        if (input.novaSenha() == null || input.novaSenha().trim().isEmpty()) {
            throw new SenhaInvalidaException("Nova senha não pode ser vazia");
        }

        String novaSenhaHash = passwordHasherService.hash(input.novaSenha());

        authenticationGateway.atualizarSenha(idUsuario, novaSenhaHash);
    }
}
