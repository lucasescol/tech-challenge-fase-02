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

import java.util.Optional;

public class AtualizarCardapioItemUseCase {

    private final ICardapioItemGateway cardapioItemGateway;
    private final IRestauranteGateway restauranteGateway;
    private final IAuthenticationGateway authenticationGateway;
    private final IUsuarioGateway usuarioGateway;

    private AtualizarCardapioItemUseCase(ICardapioItemGateway cardapioItemGateway,
                                         IRestauranteGateway restauranteGateway,
                                         IAuthenticationGateway authenticationGateway,
                                         IUsuarioGateway usuarioGateway) {
        this.cardapioItemGateway = cardapioItemGateway;
        this.restauranteGateway = restauranteGateway;
        this.authenticationGateway = authenticationGateway;
        this.usuarioGateway = usuarioGateway;
    }

    public static AtualizarCardapioItemUseCase create(ICardapioItemGateway cardapioItemGateway,
                                                       IRestauranteGateway restauranteGateway,
                                                       IAuthenticationGateway authenticationGateway,
                                                       IUsuarioGateway usuarioGateway) {
        return new AtualizarCardapioItemUseCase(cardapioItemGateway, restauranteGateway, authenticationGateway, usuarioGateway);
    }

    public Optional<OutputModel> execute(Long id, InputModel input) {
        return cardapioItemGateway.obterPorId(id)
                .map(existente -> {
                    Restaurante restaurante = restauranteGateway.obterPorId(existente.getRestauranteId())
                        .orElseThrow(() -> new RestauranteNaoEncontradoException(existente.getRestauranteId()));
                    
                    validarPermissao(restaurante.getDonoRestaurante());
                    
                    CardapioItem itemAtualizado = CardapioItem.criar(
                        id,
                        existente.getRestauranteId(),
                        input.nome(),
                        input.descricao(),
                        input.preco(),
                        input.apenasPresencial(),
                        input.caminhoFoto()
                    );
                    
                    CardapioItem itemSalvo = cardapioItemGateway.atualizar(itemAtualizado);
                    
                    return new OutputModel(
                        itemSalvo.getId(),
                        itemSalvo.getRestauranteId(),
                        itemSalvo.getNome(),
                        itemSalvo.getDescricao(),
                        itemSalvo.getPreco(),
                        itemSalvo.isApenasPresencial(),
                        itemSalvo.getCaminhoFoto()
                    );
                });
    }

    private void validarPermissao(Long donoRestauranteId) {
        if (authenticationGateway.isAdministrador()) {
            return;
        }
        
        if (!authenticationGateway.isDonoRestaurante()) {
            throw new AcessoNegadoException("Apenas donos de restaurante podem atualizar itens do cardápio");
        }
        
        String loginUsuarioLogado = authenticationGateway.getUsuarioLogado();
        Usuario usuarioLogado = usuarioGateway.buscarPorLogin(loginUsuarioLogado)
            .orElseThrow(() -> new AcessoNegadoException("Usuário não encontrado"));
        
        if (!usuarioLogado.getId().equals(donoRestauranteId)) {
            throw new AcessoNegadoException("Você só pode atualizar itens do cardápio do seu próprio restaurante");
        }
    }

    public record InputModel(
        String nome,
        String descricao,
        Double preco,
        boolean apenasPresencial,
        String caminhoFoto
    ) {}

    public record OutputModel(
        Long id,
        Long restauranteId,
        String nome,
        String descricao,
        Double preco,
        boolean apenasPresencial,
        String caminhoFoto
    ) {}
}
