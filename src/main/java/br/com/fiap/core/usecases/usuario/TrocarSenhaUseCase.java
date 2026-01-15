package br.com.fiap.core.usecases.usuario;

import br.com.fiap.core.exceptions.SenhasNaoConferemException;
import br.com.fiap.core.gateways.IUsuarioGateway;

public class TrocarSenhaUseCase {

    public record InputModel(
        String senhaAtual,
        String novaSenha,
        String confirmacaoSenha
    ) {}

    private final IUsuarioGateway usuarioGateway;

    private TrocarSenhaUseCase(IUsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public static TrocarSenhaUseCase create(IUsuarioGateway usuarioGateway) {
        return new TrocarSenhaUseCase(usuarioGateway);
    }

    public void execute(Long idUsuario, InputModel input) {
        if (!input.novaSenha().equals(input.confirmacaoSenha())) {
            throw new SenhasNaoConferemException();
        }
        this.usuarioGateway.trocarSenha(idUsuario, input.senhaAtual(), input.novaSenha());
    }
}
