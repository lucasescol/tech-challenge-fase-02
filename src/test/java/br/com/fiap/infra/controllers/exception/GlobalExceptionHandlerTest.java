package br.com.fiap.infra.controllers.exception;

import br.com.fiap.core.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("GlobalExceptionHandler - Testes Unitários")
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @Mock
    private WebRequest webRequest;

    @Mock
    private MethodArgumentNotValidException validationException;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        when(webRequest.getDescription(false)).thenReturn("uri=/api/test");
    }

    @Test
    @DisplayName("Deve tratar UsuarioNaoEncontradoException corretamente")
    void deveTratarUsuarioNaoEncontradoException() {
        UsuarioNaoEncontradoException exception = new UsuarioNaoEncontradoException(1L);

        ResponseEntity<ProblemDetail> response = exceptionHandler
                .handleUsuarioNaoEncontradoException(exception, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Usuário Não Encontrado");
        assertThat(response.getBody().getDetail()).contains("1");
    }

    @Test
    @DisplayName("Deve tratar CredenciaisInvalidasException corretamente")
    void deveTratarCredenciaisInvalidasException() {
        CredenciaisInvalidasException exception = new CredenciaisInvalidasException();

        ResponseEntity<ProblemDetail> response = exceptionHandler
                .handleCredenciaisInvalidasException(exception, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Credenciais Inválidas");
    }

    @Test
    @DisplayName("Deve tratar SenhaAtualIncorretaException corretamente")
    void deveTratarSenhaAtualIncorretaException() {
        SenhaAtualIncorretaException exception = new SenhaAtualIncorretaException();

        ResponseEntity<ProblemDetail> response = exceptionHandler
                .handleSenhaAtualIncorretaException(exception, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Senha Atual Incorreta");
    }

    @Test
    @DisplayName("Deve tratar AcessoNegadoException corretamente")
    void deveTratarAcessoNegadoException() {
        AcessoNegadoException exception = new AcessoNegadoException("Acesso negado ao recurso");

        ResponseEntity<ProblemDetail> response = exceptionHandler
                .handleAcessoNegadoException(exception, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Acesso Negado");
        assertThat(response.getBody().getDetail()).contains("Acesso negado");
    }

    @Test
    @DisplayName("Deve tratar EmailJaCadastradoException corretamente")
    void deveTratarEmailJaCadastradoException() {
        EmailJaCadastradoException exception = new EmailJaCadastradoException("teste@email.com");

        ResponseEntity<ProblemDetail> response = exceptionHandler
                .handleEmailJaCadastradoException(exception, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Email Já Cadastrado");
        assertThat(response.getBody().getDetail()).contains("teste@email.com");
    }

    @Test
    @DisplayName("Deve tratar LoginJaCadastradoException corretamente")
    void deveTratarLoginJaCadastradoException() {
        LoginJaCadastradoException exception = new LoginJaCadastradoException("usuario123");

        ResponseEntity<ProblemDetail> response = exceptionHandler
                .handleLoginJaCadastradoException(exception, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Login Já Cadastrado");
        assertThat(response.getBody().getDetail()).contains("usuario123");
    }

    @Test
    @DisplayName("Deve tratar EmailInvalidoException corretamente")
    void deveTratarEmailInvalidoException() {
        EmailInvalidoException exception = new EmailInvalidoException("Email inválido: teste");

        ResponseEntity<ProblemDetail> response = exceptionHandler
                .handleEmailInvalidoException(exception, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Email Inválido");
    }

    @Test
    @DisplayName("Deve tratar EnderecoInvalidoException corretamente")
    void deveTratarEnderecoInvalidoException() {
        EnderecoInvalidoException exception = new EnderecoInvalidoException("CEP inválido");

        ResponseEntity<ProblemDetail> response = exceptionHandler
                .handleEnderecoInvalidoException(exception, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Endereço Inválido");
    }

    @Test
    @DisplayName("Deve tratar HorarioInvalidoException corretamente")
    void deveTratarHorarioInvalidoException() {
        HorarioInvalidoException exception = new HorarioInvalidoException("Horário inválido");

        ResponseEntity<ProblemDetail> response = exceptionHandler
                .handleHorarioInvalidoException(exception, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Horário Inválido");
    }

    @Test
    @DisplayName("Deve tratar TipoCozinhaInvalidaException corretamente")
    void deveTratarTipoCozinhaInvalidaException() {
        TipoCozinhaInvalidaException exception = new TipoCozinhaInvalidaException("INVALIDA");

        ResponseEntity<ProblemDetail> response = exceptionHandler
                .handleTipoCozinhaInvalidaException(exception, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Tipo de Cozinha Inválido");
    }

    @Test
    @DisplayName("Deve tratar SenhaInvalidaException corretamente")
    void deveTratarSenhaInvalidaException() {
        SenhaInvalidaException exception = new SenhaInvalidaException("Senha deve ter no mínimo 6 caracteres");

        ResponseEntity<ProblemDetail> response = exceptionHandler
                .handleSenhaInvalidaException(exception, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Senha Inválida");
    }

    @Test
    @DisplayName("Deve tratar SenhasNaoConferemException corretamente")
    void deveTratarSenhasNaoConferemException() {
        SenhasNaoConferemException exception = new SenhasNaoConferemException();

        ResponseEntity<ProblemDetail> response = exceptionHandler
                .handleSenhasNaoConferemException(exception, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Senhas Não Conferem");
    }

    @Test
    @DisplayName("Deve tratar CampoObrigatorioException corretamente")
    void deveTratarCampoObrigatorioException() {
        CampoObrigatorioException exception = new CampoObrigatorioException("nome");

        ResponseEntity<ProblemDetail> response = exceptionHandler
                .handleCampoObrigatorioException(exception, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Campo Obrigatório");
    }

    @Test
    @DisplayName("Deve tratar TipoUsuarioInvalidoException corretamente")
    void deveTratarTipoUsuarioInvalidoException() {
        TipoUsuarioInvalidoException exception = new TipoUsuarioInvalidoException("TIPO_INVALIDO");

        ResponseEntity<ProblemDetail> response = exceptionHandler
                .handleTipoUsuarioInvalidoException(exception, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Tipo de Usuário Inválido");
    }

    @Test
    @DisplayName("Deve tratar RestauranteNaoEncontradoException corretamente")
    void deveTratarRestauranteNaoEncontradoException() {
        RestauranteNaoEncontradoException exception = new RestauranteNaoEncontradoException(5L);

        ResponseEntity<ProblemDetail> response = exceptionHandler
                .handleRestauranteNaoEncontradoException(exception, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Restaurante Não Encontrado");
        assertThat(response.getBody().getDetail()).contains("5");
    }

    @Test
    @DisplayName("Deve tratar CardapioItemNaoEncontradoException corretamente")
    void deveTratarCardapioItemNaoEncontradoException() {
        CardapioItemNaoEncontradoException exception = new CardapioItemNaoEncontradoException(10L);

        ResponseEntity<ProblemDetail> response = exceptionHandler
                .handleCardapioItemNaoEncontradoException(exception, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Item do Cardápio Não Encontrado");
        assertThat(response.getBody().getDetail()).contains("10");
    }

    @Test
    @DisplayName("Deve tratar RestauranteComCardapioException corretamente")
    void deveTratarRestauranteComCardapioException() {
        RestauranteComCardapioException exception = new RestauranteComCardapioException(3L);

        ResponseEntity<ProblemDetail> response = exceptionHandler
                .handleRestauranteComCardapioException(exception, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Restaurante com Cardápio");
        assertThat(response.getBody().getDetail()).contains("3");
    }

    @Test
    @DisplayName("Deve tratar DomainException corretamente")
    void deveTratarDomainException() {
        DomainException exception = new DomainException("Erro de validação de domínio");

        ResponseEntity<ProblemDetail> response = exceptionHandler
                .handleDomainException(exception, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Erro de Validação de Negócio");
        assertThat(response.getBody().getDetail()).contains("validação de domínio");
    }

    @Test
    @DisplayName("Deve tratar MethodArgumentNotValidException corretamente")
    void deveTratarMethodArgumentNotValidException() {
        FieldError fieldError1 = new FieldError("dto", "nome", "Nome é obrigatório");
        FieldError fieldError2 = new FieldError("dto", "email", "Email inválido");

        when(validationException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError1, fieldError2));

        ResponseEntity<ProblemDetail> response = exceptionHandler
                .handleValidationException(validationException, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Erro de Validação");
        assertThat(response.getBody().getDetail()).isEqualTo("Validação de campos falhou");

        Map<String, String> errors = (Map<String, String>) response.getBody().getProperties().get("errors");
        assertThat(errors).containsKeys("nome", "email");
        assertThat(errors.get("nome")).isEqualTo("Nome é obrigatório");
        assertThat(errors.get("email")).isEqualTo("Email inválido");
    }

    @Test
    @DisplayName("Deve tratar Exception genérica corretamente")
    void deveTratarExceptionGenerica() {
        Exception exception = new RuntimeException("Erro inesperado");

        ResponseEntity<ProblemDetail> response = exceptionHandler
                .handleGenericException(exception, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Erro Interno");
        assertThat(response.getBody().getDetail()).isEqualTo("Erro interno do servidor");
    }

    @Test
    @DisplayName("Deve incluir type e instance no ProblemDetail")
    void deveIncluirTypeEInstanceNoProblemDetail() {
        UsuarioNaoEncontradoException exception = new UsuarioNaoEncontradoException(1L);

        ResponseEntity<ProblemDetail> response = exceptionHandler
                .handleUsuarioNaoEncontradoException(exception, webRequest);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getType().toString())
                .contains("usuario-nao-encontrado");
        assertThat(response.getBody().getInstance().toString())
                .contains("/api/test");
    }
}