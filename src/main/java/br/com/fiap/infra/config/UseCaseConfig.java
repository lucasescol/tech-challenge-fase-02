package br.com.fiap.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.fiap.core.gateways.IAuthenticationGateway;
import br.com.fiap.core.gateways.IRestauranteGateway;
import br.com.fiap.core.gateways.ITipoUsuarioGateway;
import br.com.fiap.core.gateways.IUsuarioGateway;
import br.com.fiap.core.services.IPasswordHasherService;
import br.com.fiap.core.services.ITokenService;
import br.com.fiap.core.usecases.restaurante.AtualizarRestauranteUseCase;
import br.com.fiap.core.usecases.restaurante.CadastrarRestauranteUseCase;
import br.com.fiap.core.usecases.restaurante.DeletarRestauranteUseCase;
import br.com.fiap.core.usecases.restaurante.ListarTodosRestaurantesUseCase;
import br.com.fiap.core.usecases.restaurante.ObterRestaurantePorIdUseCase;
import br.com.fiap.core.usecases.tipo_usuario.AtualizarTipoUsuarioUseCase;
import br.com.fiap.core.usecases.tipo_usuario.CadastrarTipoUsuarioUseCase;
import br.com.fiap.core.usecases.tipo_usuario.DeletarTipoUsuarioUseCase;
import br.com.fiap.core.usecases.tipo_usuario.ListarTodosTiposUsuarioUseCase;
import br.com.fiap.core.usecases.tipo_usuario.ObterTipoUsuarioPorIdUseCase;
import br.com.fiap.core.usecases.usuario.AutenticarUsuarioUseCase;
import br.com.fiap.core.usecases.usuario.AtualizarUsuarioUseCase;
import br.com.fiap.core.usecases.usuario.BuscarUsuariosPorNomeUseCase;
import br.com.fiap.core.usecases.usuario.CadastrarUsuarioUseCase;
import br.com.fiap.core.usecases.usuario.ExcluirUsuarioUseCase;
import br.com.fiap.core.usecases.usuario.ListarTodosUsuariosUseCase;
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
    ObterRestaurantePorIdUseCase obterRestaurantePorIdUseCase(IRestauranteGateway restauranteGateway) {
        return ObterRestaurantePorIdUseCase.create(restauranteGateway);
    }

    @Bean
    ListarTodosRestaurantesUseCase listarTodosRestaurantesUseCase(IRestauranteGateway restauranteGateway) {
        return ListarTodosRestaurantesUseCase.create(restauranteGateway);
    }

    @Bean
    AtualizarRestauranteUseCase atualizarRestauranteUseCase(
            IRestauranteGateway restauranteGateway,
            IAuthenticationGateway authenticationGateway,
            IUsuarioGateway usuarioGateway) {
        return AtualizarRestauranteUseCase.create(restauranteGateway, authenticationGateway, usuarioGateway);
    }

    @Bean
    DeletarRestauranteUseCase deletarRestauranteUseCase(
            IRestauranteGateway restauranteGateway,
            IAuthenticationGateway authenticationGateway,
            IUsuarioGateway usuarioGateway) {
        return DeletarRestauranteUseCase.create(restauranteGateway, authenticationGateway, usuarioGateway);
    }

    @Bean
    CadastrarUsuarioUseCase cadastrarUsuarioUseCase(IUsuarioGateway usuarioGateway, ITipoUsuarioGateway tipoUsuarioGateway) {
        return CadastrarUsuarioUseCase.create(usuarioGateway, tipoUsuarioGateway);
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

    @Bean
    ListarTodosUsuariosUseCase listarTodosUsuariosUseCase(IUsuarioGateway usuarioGateway) {
        return ListarTodosUsuariosUseCase.create(usuarioGateway);
    }

    @Bean
    CadastrarTipoUsuarioUseCase cadastrarTipoUsuarioUseCase(ITipoUsuarioGateway tipoUsuarioGateway) {
        return CadastrarTipoUsuarioUseCase.create(tipoUsuarioGateway);
    }

    @Bean
    ObterTipoUsuarioPorIdUseCase obterTipoUsuarioPorIdUseCase(ITipoUsuarioGateway tipoUsuarioGateway) {
        return ObterTipoUsuarioPorIdUseCase.create(tipoUsuarioGateway);
    }

    @Bean
    ListarTodosTiposUsuarioUseCase listarTodosTiposUsuarioUseCase(ITipoUsuarioGateway tipoUsuarioGateway) {
        return ListarTodosTiposUsuarioUseCase.create(tipoUsuarioGateway);
    }

    @Bean
    AtualizarTipoUsuarioUseCase atualizarTipoUsuarioUseCase(ITipoUsuarioGateway tipoUsuarioGateway) {
        return AtualizarTipoUsuarioUseCase.create(tipoUsuarioGateway);
    }

    @Bean
    DeletarTipoUsuarioUseCase deletarTipoUsuarioUseCase(ITipoUsuarioGateway tipoUsuarioGateway) {
        return DeletarTipoUsuarioUseCase.create(tipoUsuarioGateway);
    }
}
