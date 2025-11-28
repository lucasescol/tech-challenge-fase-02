package br.com.fiap.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.fiap.core.gateways.IRestauranteGateway;
import br.com.fiap.core.usecases.CadastrarRestauranteUseCase;

@Configuration
public class UseCaseConfig {

    @Bean
    CadastrarRestauranteUseCase cadastrarRestauranteUseCase(IRestauranteGateway restauranteGateway) {
        return CadastrarRestauranteUseCase.create(restauranteGateway);
    }
}
