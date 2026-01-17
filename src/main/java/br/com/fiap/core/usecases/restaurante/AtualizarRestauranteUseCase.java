package br.com.fiap.core.usecases.restaurante;

import br.com.fiap.core.domain.Restaurante;
import br.com.fiap.core.domain.Usuario;
import br.com.fiap.core.exceptions.AcessoNegadoException;
import br.com.fiap.core.gateways.IAuthenticationGateway;
import br.com.fiap.core.gateways.IRestauranteGateway;
import br.com.fiap.core.gateways.IUsuarioGateway;

import java.util.Optional;

public class AtualizarRestauranteUseCase {

    private final IRestauranteGateway restauranteGateway;
    private final IAuthenticationGateway authenticationGateway;
    private final IUsuarioGateway usuarioGateway;

    private AtualizarRestauranteUseCase(IRestauranteGateway restauranteGateway, 
                                       IAuthenticationGateway authenticationGateway,
                                       IUsuarioGateway usuarioGateway) {
        this.restauranteGateway = restauranteGateway;
        this.authenticationGateway = authenticationGateway;
        this.usuarioGateway = usuarioGateway;
    }
    
    public static AtualizarRestauranteUseCase create(IRestauranteGateway restauranteGateway,
                                                     IAuthenticationGateway authenticationGateway,
                                                     IUsuarioGateway usuarioGateway) {
        return new AtualizarRestauranteUseCase(restauranteGateway, authenticationGateway, usuarioGateway);
    }

    public Optional<OutputModel> execute(Long id, InputModel input) {
        return restauranteGateway.obterPorId(id)
                .map(existente -> {
                    validarPermissao(existente);
                    
                    Restaurante restauranteAtualizado = Restaurante.create(
                        id,
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
                        existente.getDonoRestaurante()
                    );
                    
                    Restaurante restauranteSalvo = restauranteGateway.atualizar(restauranteAtualizado);
                    
                    return new OutputModel(
                        restauranteSalvo.getId(),
                        restauranteSalvo.getNome(),
                        restauranteSalvo.getEndereco().getEnderecoCompleto(),
                        restauranteSalvo.getTipoCozinha().getValor(),
                        restauranteSalvo.getHorarioFuncionamento().getValor()
                    );
                });
    }
    
    private void validarPermissao(Restaurante restaurante) {
        String loginUsuarioLogado = authenticationGateway.getUsuarioLogado();
        Usuario usuarioLogado = usuarioGateway.buscarPorLogin(loginUsuarioLogado)
            .orElseThrow(() -> new AcessoNegadoException("Usuário não encontrado"));
        
        boolean isProprietario = restaurante.getDonoRestaurante().equals(usuarioLogado.getId());
        boolean isAdministrador = authenticationGateway.isAdministrador();
        
        if (!isProprietario && !isAdministrador) {
            throw new AcessoNegadoException("Apenas o proprietário do restaurante ou administradores podem atualizar este restaurante");
        }
    }

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
}
