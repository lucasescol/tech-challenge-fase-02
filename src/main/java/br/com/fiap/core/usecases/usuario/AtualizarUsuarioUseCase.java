package br.com.fiap.core.usecases.usuario;

import br.com.fiap.core.domain.Usuario;
import br.com.fiap.core.domain.Endereco;
import br.com.fiap.core.exceptions.UsuarioNaoEncontradoException;
import br.com.fiap.core.gateways.IUsuarioGateway;

public class AtualizarUsuarioUseCase {

    public record InputModel(
            String nome,
            String email,
            String login,
            Endereco endereco) {
    }

    private final IUsuarioGateway usuarioGateway;

    public AtualizarUsuarioUseCase(IUsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public Usuario executar(Long idUsuario, InputModel input) {
        Usuario usuarioExistente = this.usuarioGateway.buscarPorId(idUsuario)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado com ID: " + idUsuario));

        Usuario usuarioAtualizado = Usuario.criar(
                idUsuario,
                input.nome,
                input.email,
                input.login,
                usuarioExistente.getSenha(),
                input.endereco,
                usuarioExistente.getTipoUsuario());

        return this.usuarioGateway.atualizar(usuarioAtualizado);
    }
}
