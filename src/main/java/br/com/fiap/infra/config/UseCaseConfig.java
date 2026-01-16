package br.com.fiap.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.fiap.core.gateways.IRestauranteGateway;
import br.com.fiap.core.gateways.ITipoUsuarioGateway;
import br.com.fiap.core.gateways.IUsuarioGateway;
import br.com.fiap.core.gateways.ICardapioItemGateway;
import br.com.fiap.core.gateways.IAuthenticationGateway;
import br.com.fiap.core.services.IPasswordHasherService;
import br.com.fiap.core.services.ITokenService;
import br.com.fiap.core.usecases.CadastrarRestauranteUseCase;
import br.com.fiap.core.usecases.CadastrarTipoUsuarioUseCase;
import br.com.fiap.core.usecases.CadastrarUsuarioUseCase;
import br.com.fiap.core.usecases.CadastrarCardapioItemUseCase;
import br.com.fiap.core.usecases.usuario.TrocarSenhaUseCase;
import br.com.fiap.core.usecases.usuario.AutenticarUsuarioUseCase;
import br.com.fiap.core.usecases.usuario.AtualizarUsuarioUseCase;
import br.com.fiap.core.usecases.usuario.BuscarUsuariosPorNomeUseCase;

@Configuration
public class UseCaseConfig {

    @Bean
    CadastrarRestauranteUseCase cadastrarRestauranteUseCase(IRestauranteGateway restauranteGateway) {
        return CadastrarRestauranteUseCase.create(restauranteGateway);
    }

    @Bean
    CadastrarTipoUsuarioUseCase cadastrarTipoUsuarioUseCase(ITipoUsuarioGateway tipoUsuarioGateway) {
        return new CadastrarTipoUsuarioUseCase(tipoUsuarioGateway);
    }

    @Bean
    CadastrarUsuarioUseCase cadastrarUsuarioUseCase(IUsuarioGateway usuarioGateway,
            IPasswordHasherService passwordHasherService) {
        return new CadastrarUsuarioUseCase(usuarioGateway, passwordHasherService);
    }

    @Bean
    CadastrarCardapioItemUseCase cadastrarCardapioItemUseCase(ICardapioItemGateway cardapioItemGateway) {
        return new CadastrarCardapioItemUseCase(cardapioItemGateway);
    }

    @Bean
    TrocarSenhaUseCase trocarSenhaUseCase(IAuthenticationGateway authenticationGateway,
            IPasswordHasherService passwordHasherService) {
        return new TrocarSenhaUseCase(authenticationGateway, passwordHasherService);
    }

    @Bean
    AutenticarUsuarioUseCase autenticarUsuarioUseCase(IAuthenticationGateway authenticationGateway,
            IPasswordHasherService passwordHasherService, ITokenService tokenService) {
        return new AutenticarUsuarioUseCase(authenticationGateway, passwordHasherService, tokenService);
    }

    @Bean
    AtualizarUsuarioUseCase atualizarUsuarioUseCase(IUsuarioGateway usuarioGateway) {
        return new AtualizarUsuarioUseCase(usuarioGateway);
    }

    @Bean
    BuscarUsuariosPorNomeUseCase buscarUsuariosPorNomeUseCase(IUsuarioGateway usuarioGateway) {
        return new BuscarUsuariosPorNomeUseCase(usuarioGateway);
    }
}
