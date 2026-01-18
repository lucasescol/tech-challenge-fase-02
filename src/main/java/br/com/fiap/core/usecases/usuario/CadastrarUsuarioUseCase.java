package br.com.fiap.core.usecases.usuario;

import br.com.fiap.core.domain.Endereco;
import br.com.fiap.core.domain.TipoUsuario;
import br.com.fiap.core.domain.Usuario;
import br.com.fiap.core.exceptions.EmailJaCadastradoException;
import br.com.fiap.core.exceptions.LoginJaCadastradoException;
import br.com.fiap.core.exceptions.TipoUsuarioInvalidoException;
import br.com.fiap.core.gateways.ITipoUsuarioGateway;
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

    public record OutputModel(
        Long id,
        String nome,
        String email,
        String login,
        String endereco,
        String tipoUsuario
    ) {}

    private final IUsuarioGateway usuarioGateway;
    private final ITipoUsuarioGateway tipoUsuarioGateway;

    private CadastrarUsuarioUseCase(IUsuarioGateway usuarioGateway, ITipoUsuarioGateway tipoUsuarioGateway) {
        this.usuarioGateway = usuarioGateway;
        this.tipoUsuarioGateway = tipoUsuarioGateway;
    }

    public static CadastrarUsuarioUseCase create(IUsuarioGateway usuarioGateway, ITipoUsuarioGateway tipoUsuarioGateway) {
        return new CadastrarUsuarioUseCase(usuarioGateway, tipoUsuarioGateway);
    }

    public OutputModel execute(InputModel input) {
        if (usuarioGateway.existeLoginCadastrado(input.login(), null)) {
            throw new LoginJaCadastradoException(input.login());
        }
        
        if (usuarioGateway.existeEmailCadastrado(input.email(), null)) {
            throw new EmailJaCadastradoException(input.email());
        }
        
        Endereco endereco = new Endereco(
            input.logradouro(),
            input.numero(),
            input.complemento(),
            input.bairro(),
            input.cidade(),
            input.estado(),
            input.cep()
        );


        TipoUsuario tipoUsuario = tipoUsuarioGateway.obterPorNome(input.tipoUsuario().toUpperCase())
            .orElseThrow(() -> new TipoUsuarioInvalidoException(input.tipoUsuario()));

        Usuario novoUsuario = Usuario.create(
            null,
            input.nome(),
            input.email(),
            input.login(),
            input.senha(),
            endereco,
            tipoUsuario
        );
        
        Usuario usuarioSalvo = this.usuarioGateway.incluir(novoUsuario);

        return new OutputModel(
            usuarioSalvo.getId(),
            usuarioSalvo.getNome(),
            usuarioSalvo.getEmail().getValor(),
            usuarioSalvo.getLogin(),
            usuarioSalvo.getEndereco().getEnderecoCompleto(),
            usuarioSalvo.getTipoUsuario().getNome()
        );
    }
}
