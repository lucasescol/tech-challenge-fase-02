package br.com.fiap.core.usecases;

import br.com.fiap.core.domain.Usuario;
import br.com.fiap.core.gateways.IUsuarioGateway;
import br.com.fiap.core.services.IPasswordHasherService;

public class CadastrarUsuarioUseCase {

    private final IUsuarioGateway usuarioGateway;
    private final IPasswordHasherService passwordHasherService;

    public CadastrarUsuarioUseCase(IUsuarioGateway usuarioGateway, IPasswordHasherService passwordHasherService) {
        this.usuarioGateway = usuarioGateway;
        this.passwordHasherService = passwordHasherService;
    }

    public Usuario executar(Usuario usuario) {

        String senhaHash = passwordHasherService.hash(usuario.getSenha());

        Usuario usuarioComSenhaHasheada = Usuario.criar(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail().getValor(),
                usuario.getLogin(),
                senhaHash,
                usuario.getEndereco(),
                usuario.getTipoUsuario());

        return usuarioGateway.incluir(usuarioComSenhaHasheada);
    }
}
