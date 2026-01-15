package br.com.fiap.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.fiap.core.gateways.IRestauranteGateway;
import br.com.fiap.core.gateways.IUsuarioGateway;
import br.com.fiap.core.usecases.restaurante.CadastrarRestauranteUseCase;
import br.com.fiap.core.usecases.usuario.AtualizarUsuarioUseCase;
import br.com.fiap.core.usecases.usuario.BuscarUsuariosPorNomeUseCase;
import br.com.fiap.core.usecases.usuario.CadastrarUsuarioUseCase;
import br.com.fiap.core.usecases.usuario.ExcluirUsuarioUseCase;
import br.com.fiap.core.usecases.usuario.TrocarSenhaUseCase;

@Configuration
public class UseCaseConfig {

    @Bean
    CadastrarRestauranteUseCase cadastrarRestauranteUseCase(IRestauranteGateway restauranteGateway) {
        return CadastrarRestauranteUseCase.create(restauranteGateway);
    }

    @Bean
    CadastrarUsuarioUseCase cadastrarUsuarioUseCase(IUsuarioGateway usuarioGateway) {
        return CadastrarUsuarioUseCase.create(usuarioGateway);
    }

    @Bean
    AtualizarUsuarioUseCase atualizarUsuarioUseCase(IUsuarioGateway usuarioGateway) {
        return AtualizarUsuarioUseCase.create(usuarioGateway);
    }

    @Bean
    ExcluirUsuarioUseCase excluirUsuarioUseCase(IUsuarioGateway usuarioGateway) {
        return ExcluirUsuarioUseCase.create(usuarioGateway);
    }

    @Bean
    TrocarSenhaUseCase trocarSenhaUseCase(IUsuarioGateway usuarioGateway) {
        return TrocarSenhaUseCase.create(usuarioGateway);
    }

    @Bean
    BuscarUsuariosPorNomeUseCase buscarUsuariosPorNomeUseCase(IUsuarioGateway usuarioGateway) {
        return BuscarUsuariosPorNomeUseCase.create(usuarioGateway);
    }
}
