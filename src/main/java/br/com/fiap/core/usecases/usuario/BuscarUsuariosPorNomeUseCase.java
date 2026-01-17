package br.com.fiap.core.usecases.usuario;

import java.util.List;
import java.util.stream.Collectors;

import br.com.fiap.core.gateways.IUsuarioGateway;

public class BuscarUsuariosPorNomeUseCase {
    private final IUsuarioGateway usuarioGateway;

    private BuscarUsuariosPorNomeUseCase(IUsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public static BuscarUsuariosPorNomeUseCase create(IUsuarioGateway usuarioGateway) {
        return new BuscarUsuariosPorNomeUseCase(usuarioGateway);
    }

    public List<OutputModel> execute(String nome) {
        return this.usuarioGateway.buscarPorNome(nome)
                .stream()
                .map(usuario -> new OutputModel(
                    usuario.getId(),
                    usuario.getNome(),
                    usuario.getEmail().getValor(),
                    usuario.getLogin(),
                    usuario.getEndereco().getEnderecoCompleto(),
                    usuario.getTipoUsuario().getNome()
                ))
                .collect(Collectors.toList());
    }

    public record OutputModel(
        Long id,
        String nome,
        String email,
        String login,
        String endereco,
        String tipoUsuario
    ) {}
}
