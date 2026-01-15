package br.com.fiap.core.usecases.usuario;

import br.com.fiap.core.domain.Endereco;
import br.com.fiap.core.domain.Usuario;
import br.com.fiap.core.gateways.IUsuarioGateway;

public class CadastrarUsuarioUseCase {

    public record InputModel(
        String nome,
        String email,
        String login,
        String senha,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String estado,
        String cep,
        String tipoUsuario
    ) {}

    private final IUsuarioGateway usuarioGateway;

    private CadastrarUsuarioUseCase(IUsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public static CadastrarUsuarioUseCase create(IUsuarioGateway usuarioGateway) {
        return new CadastrarUsuarioUseCase(usuarioGateway);
    }

    public Usuario execute(InputModel input) {
        Endereco endereco = new Endereco(
            input.logradouro(),
            input.numero(),
            input.complemento(),
            input.bairro(),
            input.cidade(),
            input.estado(),
            input.cep()
        );
        
        Usuario.TipoUsuario tipoUsuario = Usuario.TipoUsuario.valueOf(
            input.tipoUsuario().toUpperCase()
        );
        
        Usuario novoUsuario = Usuario.create(
            null,
            input.nome(),
            input.email(),
            input.login(),
            input.senha(),
            endereco,
            tipoUsuario
        );
        
        return this.usuarioGateway.incluir(novoUsuario);
    }
}
