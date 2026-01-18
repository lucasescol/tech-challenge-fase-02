package br.com.fiap.core.usecases.restaurante;

import br.com.fiap.core.domain.Restaurante;
import br.com.fiap.core.domain.Usuario;
import br.com.fiap.core.exceptions.AcessoNegadoException;
import br.com.fiap.core.exceptions.RestauranteComCardapioException;
import br.com.fiap.core.exceptions.RestauranteNaoEncontradoException;
import br.com.fiap.core.gateways.IAuthenticationGateway;
import br.com.fiap.core.gateways.ICardapioItemGateway;
import br.com.fiap.core.gateways.IRestauranteGateway;
import br.com.fiap.core.gateways.IUsuarioGateway;

public class DeletarRestauranteUseCase {

    private final IRestauranteGateway restauranteGateway;
    private final IAuthenticationGateway authenticationGateway;
    private final IUsuarioGateway usuarioGateway;
    private final ICardapioItemGateway cardapioItemGateway;

    private DeletarRestauranteUseCase(IRestauranteGateway restauranteGateway,
                                     IAuthenticationGateway authenticationGateway,
                                     IUsuarioGateway usuarioGateway,
                                     ICardapioItemGateway cardapioItemGateway) {
        this.restauranteGateway = restauranteGateway;
        this.authenticationGateway = authenticationGateway;
        this.usuarioGateway = usuarioGateway;
        this.cardapioItemGateway = cardapioItemGateway;
    }
    
    public static DeletarRestauranteUseCase create(IRestauranteGateway restauranteGateway,
                                                   IAuthenticationGateway authenticationGateway,
                                                   IUsuarioGateway usuarioGateway,
                                                   ICardapioItemGateway cardapioItemGateway) {
        return new DeletarRestauranteUseCase(restauranteGateway, authenticationGateway, usuarioGateway, cardapioItemGateway);
    }

    public void execute(Long id) {
        Restaurante restaurante = restauranteGateway.obterPorId(id)
            .orElseThrow(() -> new RestauranteNaoEncontradoException(id));
        
        validarPermissao(restaurante);
        validarRestauranteSemCardapio(id);
        restauranteGateway.deletar(id);
    }
    
    private void validarRestauranteSemCardapio(Long restauranteId) {
        var itensCardapio = cardapioItemGateway.listarPorRestaurante(restauranteId);
        if (!itensCardapio.isEmpty()) {
            throw new RestauranteComCardapioException(restauranteId);
        }
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
