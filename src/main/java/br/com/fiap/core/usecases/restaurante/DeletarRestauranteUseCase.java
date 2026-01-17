package br.com.fiap.core.usecases.restaurante;

import br.com.fiap.core.domain.Restaurante;
import br.com.fiap.core.domain.Usuario;
import br.com.fiap.core.exceptions.AcessoNegadoException;
import br.com.fiap.core.gateways.IAuthenticationGateway;
import br.com.fiap.core.gateways.IRestauranteGateway;
import br.com.fiap.core.gateways.IUsuarioGateway;

public class DeletarRestauranteUseCase {

    private final IRestauranteGateway restauranteGateway;
    private final IAuthenticationGateway authenticationGateway;
    private final IUsuarioGateway usuarioGateway;

    private DeletarRestauranteUseCase(IRestauranteGateway restauranteGateway,
                                     IAuthenticationGateway authenticationGateway,
                                     IUsuarioGateway usuarioGateway) {
        this.restauranteGateway = restauranteGateway;
        this.authenticationGateway = authenticationGateway;
        this.usuarioGateway = usuarioGateway;
    }
    
    public static DeletarRestauranteUseCase create(IRestauranteGateway restauranteGateway,
                                                   IAuthenticationGateway authenticationGateway,
                                                   IUsuarioGateway usuarioGateway) {
        return new DeletarRestauranteUseCase(restauranteGateway, authenticationGateway, usuarioGateway);
    }

    public boolean execute(Long id) {
        return restauranteGateway.obterPorId(id)
                .map(restaurante -> {
                    validarPermissao(restaurante);
                    restauranteGateway.deletar(id);
                    return true;
                })
                .orElse(false);
    }
    
    private void validarPermissao(Restaurante restaurante) {
        String loginUsuarioLogado = authenticationGateway.getUsuarioLogado();
        Usuario usuarioLogado = usuarioGateway.buscarPorLogin(loginUsuarioLogado)
            .orElseThrow(() -> new AcessoNegadoException("Usuário não encontrado"));
        
        boolean isProprietario = restaurante.getDonoRestaurante().equals(usuarioLogado.getId());
        boolean isAdministrador = authenticationGateway.isAdministrador();
        
        if (!isProprietario && !isAdministrador) {
            throw new AcessoNegadoException("Apenas o proprietário do restaurante ou administradores podem deletar este restaurante");
        }
    }
}
