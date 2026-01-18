package br.com.fiap.core.usecases.cardapio_item;

import br.com.fiap.core.domain.CardapioItem;
import br.com.fiap.core.domain.Restaurante;
import br.com.fiap.core.domain.Usuario;
import br.com.fiap.core.exceptions.AcessoNegadoException;
import br.com.fiap.core.exceptions.RestauranteNaoEncontradoException;
import br.com.fiap.core.gateways.IAuthenticationGateway;
import br.com.fiap.core.gateways.ICardapioItemGateway;
import br.com.fiap.core.gateways.IRestauranteGateway;
import br.com.fiap.core.gateways.IUsuarioGateway;

public class CadastrarCardapioItemUseCase {

    private final ICardapioItemGateway cardapioItemGateway;
    private final IRestauranteGateway restauranteGateway;
    private final IAuthenticationGateway authenticationGateway;
    private final IUsuarioGateway usuarioGateway;

    private CadastrarCardapioItemUseCase(ICardapioItemGateway cardapioItemGateway, 
                                         IRestauranteGateway restauranteGateway,
                                         IAuthenticationGateway authenticationGateway,
                                         IUsuarioGateway usuarioGateway) {
        this.cardapioItemGateway = cardapioItemGateway;
        this.restauranteGateway = restauranteGateway;
        this.authenticationGateway = authenticationGateway;
        this.usuarioGateway = usuarioGateway;
    }

    public static CadastrarCardapioItemUseCase create(ICardapioItemGateway cardapioItemGateway,
                                                      IRestauranteGateway restauranteGateway,
                                                      IAuthenticationGateway authenticationGateway,
                                                      IUsuarioGateway usuarioGateway) {
        return new CadastrarCardapioItemUseCase(cardapioItemGateway, restauranteGateway, authenticationGateway, usuarioGateway);
    }

    public CardapioItem execute(CardapioItem item) {
        Restaurante restaurante = restauranteGateway.obterPorId(item.getRestauranteId())
            .orElseThrow(() -> new RestauranteNaoEncontradoException(item.getRestauranteId()));
        
        validarPermissao(restaurante.getDonoRestaurante());
        
        return cardapioItemGateway.incluir(item);
    }
    
    private void validarPermissao(Long donoRestauranteId) {
        if (authenticationGateway.isAdministrador()) {
            return;
        }
        
        if (!authenticationGateway.isDonoRestaurante()) {
            throw new AcessoNegadoException("Apenas donos de restaurante podem cadastrar itens no cardápio");
        }
        
        String loginUsuarioLogado = authenticationGateway.getUsuarioLogado();
        Usuario usuarioLogado = usuarioGateway.buscarPorLogin(loginUsuarioLogado)
            .orElseThrow(() -> new AcessoNegadoException("Usuário não encontrado"));
        
        if (!usuarioLogado.getId().equals(donoRestauranteId)) {
            throw new AcessoNegadoException("Você só pode adicionar itens ao cardápio do seu próprio restaurante");
        }
    }
}
