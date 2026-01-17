package br.com.fiap.core.usecases.restaurante;

import br.com.fiap.core.domain.Restaurante;
import br.com.fiap.core.domain.Usuario;
import br.com.fiap.core.exceptions.AcessoNegadoException;
import br.com.fiap.core.gateways.IAuthenticationGateway;
import br.com.fiap.core.gateways.IRestauranteGateway;
import br.com.fiap.core.gateways.IUsuarioGateway;

public class CadastrarRestauranteUseCase {
    private final IRestauranteGateway restauranteGateway;
    private final IAuthenticationGateway authenticationGateway;
    private final IUsuarioGateway usuarioGateway;

    public record InputModel(
        String nome,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String estado,
        String cep,
        String tipoCozinha,
        String horarioFuncionamento
    ) {}

    public record OutputModel(
        Long id,
        String nome,
        String endereco,
        String tipoCozinha,
        String horarioFuncionamento
    ) {}

    private CadastrarRestauranteUseCase(IRestauranteGateway restauranteGateway, IAuthenticationGateway authenticationGateway, IUsuarioGateway usuarioGateway) {
        this.restauranteGateway = restauranteGateway;
        this.authenticationGateway = authenticationGateway;
        this.usuarioGateway = usuarioGateway;
    }

    public static CadastrarRestauranteUseCase create(IRestauranteGateway restauranteGateway, IAuthenticationGateway authenticationGateway, IUsuarioGateway usuarioGateway) {
        return new CadastrarRestauranteUseCase(restauranteGateway, authenticationGateway, usuarioGateway);
    }

    public OutputModel execute(InputModel input) {
        validarPermissao();
        
        String loginUsuarioLogado = authenticationGateway.getUsuarioLogado();
        Usuario usuarioLogado = usuarioGateway.buscarPorLogin(loginUsuarioLogado)
            .orElseThrow(() -> new AcessoNegadoException("Usuário não encontrado"));
        
        Restaurante novoRestaurante = Restaurante.create(
            null,
            input.nome(),
            input.logradouro(),
            input.numero(),
            input.complemento(),
            input.bairro(),
            input.cidade(),
            input.estado(),
            input.cep(),
            input.tipoCozinha(),
            input.horarioFuncionamento(),
            usuarioLogado.getId()
        );

        Restaurante restauranteSalvo = this.restauranteGateway.incluir(novoRestaurante);

        return new OutputModel(
            restauranteSalvo.getId(),
            restauranteSalvo.getNome(),
            restauranteSalvo.getEndereco().getEnderecoCompleto(),
            restauranteSalvo.getTipoCozinha().getValor(),
            restauranteSalvo.getHorarioFuncionamento().getValor()
        );
    }
    
    private void validarPermissao() {
        if (!authenticationGateway.isDonoRestaurante() && !authenticationGateway.isAdministrador()) {
            throw new AcessoNegadoException("Apenas donos de restaurante e administradores podem cadastrar restaurantes");
        }
    }
}
