package br.com.fiap.core.usecases.cardapio_item;

import br.com.fiap.core.domain.CardapioItem;
import br.com.fiap.core.domain.Restaurante;
import br.com.fiap.core.domain.Usuario;
import br.com.fiap.core.exceptions.AcessoNegadoException;
import br.com.fiap.core.exceptions.CardapioItemNaoEncontradoException;
import br.com.fiap.core.exceptions.RestauranteNaoEncontradoException;
import br.com.fiap.core.gateways.IAuthenticationGateway;
import br.com.fiap.core.gateways.ICardapioItemGateway;
import br.com.fiap.core.gateways.IRestauranteGateway;
import br.com.fiap.core.gateways.IUsuarioGateway;

public class DeletarCardapioItemUseCase {

    private final ICardapioItemGateway cardapioItemGateway;
    private final IRestauranteGateway restauranteGateway;
    private final IAuthenticationGateway authenticationGateway;
    private final IUsuarioGateway usuarioGateway;

    private DeletarCardapioItemUseCase(ICardapioItemGateway cardapioItemGateway,
                                       IRestauranteGateway restauranteGateway,
                                       IAuthenticationGateway authenticationGateway,
                                       IUsuarioGateway usuarioGateway) {
        this.cardapioItemGateway = cardapioItemGateway;
        this.restauranteGateway = restauranteGateway;
        this.authenticationGateway = authenticationGateway;
        this.usuarioGateway = usuarioGateway;
    }

    public static DeletarCardapioItemUseCase create(ICardapioItemGateway cardapioItemGateway,
                                                     IRestauranteGateway restauranteGateway,
                                                     IAuthenticationGateway authenticationGateway,
                                                     IUsuarioGateway usuarioGateway) {
        return new DeletarCardapioItemUseCase(cardapioItemGateway, restauranteGateway, authenticationGateway, usuarioGateway);
    }

    public void execute(Long id) {
        CardapioItem item = cardapioItemGateway.obterPorId(id)
            .orElseThrow(() -> new CardapioItemNaoEncontradoException(id));
        
        Restaurante restaurante = restauranteGateway.obterPorId(item.getRestauranteId())
            .orElseThrow(() -> new RestauranteNaoEncontradoException(item.getRestauranteId()));
        
        validarPermissao(restaurante.getDonoRestaurante());
        
        cardapioItemGateway.deletar(id);
    }

    private void validarPermissao(Long donoRestauranteId) {
        if (authenticationGateway.isAdministrador()) {
            return;
        }
        
        if (!authenticationGateway.isDonoRestaurante()) {
            throw new AcessoNegadoException("Apenas donos de restaurante podem deletar itens do cardápio");
        }
        
        String loginUsuarioLogado = authenticationGateway.getUsuarioLogado();
        Usuario usuarioLogado = usuarioGateway.buscarPorLogin(loginUsuarioLogado)
            .orElseThrow(() -> new AcessoNegadoException("Usuário não encontrado"));
        
        if (!usuarioLogado.getId().equals(donoRestauranteId)) {
            throw new AcessoNegadoException("Você só pode deletar itens do cardápio do seu próprio restaurante");
        }
    }
}
