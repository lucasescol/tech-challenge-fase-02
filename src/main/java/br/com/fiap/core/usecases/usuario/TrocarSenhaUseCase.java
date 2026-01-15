package br.com.fiap.core.usecases.usuario;

import br.com.fiap.core.domain.Usuario;
import br.com.fiap.core.exceptions.AcessoNegadoException;
import br.com.fiap.core.exceptions.SenhaAtualIncorretaException;
import br.com.fiap.core.exceptions.UsuarioNaoEncontradoException;
import br.com.fiap.core.gateways.IAuthenticationGateway;
import br.com.fiap.core.gateways.IUsuarioGateway;
import br.com.fiap.core.services.IPasswordHasherService;

public class TrocarSenhaUseCase {

    public record InputModel(
        String senhaAtual,
        String novaSenha,
        String confirmacaoSenha
    ) {}

    private final IUsuarioGateway usuarioGateway;
    private final IPasswordHasherService passwordHasherService;
    private final IAuthenticationGateway authenticationGateway;

    private TrocarSenhaUseCase(IUsuarioGateway usuarioGateway, IPasswordHasherService passwordHasherService, IAuthenticationGateway authenticationGateway) {
        this.usuarioGateway = usuarioGateway;
        this.passwordHasherService = passwordHasherService;
        this.authenticationGateway = authenticationGateway;
    }

    public static TrocarSenhaUseCase create(IUsuarioGateway usuarioGateway, IPasswordHasherService passwordHasherService, IAuthenticationGateway authenticationGateway) {
        return new TrocarSenhaUseCase(usuarioGateway, passwordHasherService, authenticationGateway);
    }

    public void execute(Long idUsuario, InputModel input) {
        validarPermissao(idUsuario);
        
        Usuario.validarNovaSenha(input.novaSenha(), input.confirmacaoSenha());
        
        Usuario usuario = this.usuarioGateway.buscarPorId(idUsuario)
            .orElseThrow(() -> new UsuarioNaoEncontradoException(idUsuario));
        
        if (!passwordHasherService.matches(input.senhaAtual(), usuario.getSenha())) {
            throw new SenhaAtualIncorretaException();
        }
        
        String novaSenhaCriptografada = passwordHasherService.encode(input.novaSenha());
        this.usuarioGateway.trocarSenha(idUsuario, novaSenhaCriptografada);
    }
    
    private void validarPermissao(Long idUsuario) {
        if (authenticationGateway.isAdministrador()) {
            return;
        }
        
        Usuario usuario = usuarioGateway.buscarPorId(idUsuario)
            .orElseThrow(() -> new UsuarioNaoEncontradoException(idUsuario));
        
        String loginUsuarioLogado = authenticationGateway.getUsuarioLogado();
        
        if (!usuario.getLogin().equals(loginUsuarioLogado)) {
            throw new AcessoNegadoException("Você só pode trocar sua própria senha");
        }
    }
}
