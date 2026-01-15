package br.com.fiap.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.fiap.core.gateways.IAuthenticationGateway;
import br.com.fiap.core.gateways.IRestauranteGateway;
import br.com.fiap.core.gateways.IUsuarioGateway;
import br.com.fiap.core.services.IPasswordHasherService;
import br.com.fiap.core.services.ITokenService;
import br.com.fiap.core.usecases.restaurante.CadastrarRestauranteUseCase;
import br.com.fiap.core.usecases.usuario.AutenticarUsuarioUseCase;
import br.com.fiap.core.usecases.usuario.AtualizarUsuarioUseCase;
import br.com.fiap.core.usecases.usuario.BuscarUsuariosPorNomeUseCase;
import br.com.fiap.core.usecases.usuario.CadastrarUsuarioUseCase;
import br.com.fiap.core.usecases.usuario.ExcluirUsuarioUseCase;
import br.com.fiap.core.usecases.usuario.TrocarSenhaUseCase;

@Configuration
public class UseCaseConfig {

    @Bean
    CadastrarRestauranteUseCase cadastrarRestauranteUseCase(
            IRestauranteGateway restauranteGateway,
            IAuthenticationGateway authenticationGateway,
            IUsuarioGateway usuarioGateway) {
        return CadastrarRestauranteUseCase.create(restauranteGateway, authenticationGateway, usuarioGateway);
    }

    @Bean
    CadastrarUsuarioUseCase cadastrarUsuarioUseCase(IUsuarioGateway usuarioGateway) {
        return CadastrarUsuarioUseCase.create(usuarioGateway);
    }

    @Bean
    AtualizarUsuarioUseCase atualizarUsuarioUseCase(
            IUsuarioGateway usuarioGateway,
            IAuthenticationGateway authenticationGateway) {
        return AtualizarUsuarioUseCase.create(usuarioGateway, authenticationGateway);
    }

    @Bean
    ExcluirUsuarioUseCase excluirUsuarioUseCase(
            IUsuarioGateway usuarioGateway,
            IAuthenticationGateway authenticationGateway) {
        return ExcluirUsuarioUseCase.create(usuarioGateway, authenticationGateway);
    }

    @Bean
    TrocarSenhaUseCase trocarSenhaUseCase(
            IUsuarioGateway usuarioGateway, 
            IPasswordHasherService passwordHasherService,
            IAuthenticationGateway authenticationGateway) {
        return TrocarSenhaUseCase.create(usuarioGateway, passwordHasherService, authenticationGateway);
    }

    @Bean
    AutenticarUsuarioUseCase autenticarUsuarioUseCase(IUsuarioGateway usuarioGateway, 
                                                       IPasswordHasherService passwordHasherService,
                                                       ITokenService tokenService) {
        return AutenticarUsuarioUseCase.create(usuarioGateway, passwordHasherService, tokenService);
    }

    @Bean
    BuscarUsuariosPorNomeUseCase buscarUsuariosPorNomeUseCase(IUsuarioGateway usuarioGateway) {
        return BuscarUsuariosPorNomeUseCase.create(usuarioGateway);
    }
}
