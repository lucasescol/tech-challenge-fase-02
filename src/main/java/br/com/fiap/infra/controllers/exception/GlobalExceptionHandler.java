package br.com.fiap.infra.controllers.exception;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import br.com.fiap.core.exceptions.AcessoNegadoException;
import br.com.fiap.core.exceptions.CampoObrigatorioException;
import br.com.fiap.core.exceptions.CardapioItemNaoEncontradoException;
import br.com.fiap.core.exceptions.CredenciaisInvalidasException;
import br.com.fiap.core.exceptions.DomainException;
import br.com.fiap.core.exceptions.EmailInvalidoException;
import br.com.fiap.core.exceptions.EmailJaCadastradoException;
import br.com.fiap.core.exceptions.EnderecoInvalidoException;
import br.com.fiap.core.exceptions.HorarioInvalidoException;
import br.com.fiap.core.exceptions.LoginJaCadastradoException;
import br.com.fiap.core.exceptions.RestauranteComCardapioException;
import br.com.fiap.core.exceptions.RestauranteNaoEncontradoException;
import br.com.fiap.core.exceptions.SenhaAtualIncorretaException;
import br.com.fiap.core.exceptions.SenhaInvalidaException;
import br.com.fiap.core.exceptions.SenhasNaoConferemException;
import br.com.fiap.core.exceptions.TipoCozinhaInvalidaException;
import br.com.fiap.core.exceptions.TipoUsuarioInvalidoException;
import br.com.fiap.core.exceptions.UsuarioNaoEncontradoException;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(UsuarioNaoEncontradoException.class)
        public ResponseEntity<ProblemDetail> handleUsuarioNaoEncontradoException(
                        UsuarioNaoEncontradoException ex, WebRequest request) {

                ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                                HttpStatus.NOT_FOUND,
                                ex.getMessage());

                problem.setType(URI.create("https://api.techchallenge.com/errors/usuario-nao-encontrado"));
                problem.setTitle("Usuário Não Encontrado");
                problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
        }

        @ExceptionHandler(CredenciaisInvalidasException.class)
        public ResponseEntity<ProblemDetail> handleCredenciaisInvalidasException(
                        CredenciaisInvalidasException ex, WebRequest request) {

                ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                                HttpStatus.UNAUTHORIZED,
                                ex.getMessage());

                problem.setType(URI.create("https://api.techchallenge.com/errors/credenciais-invalidas"));
                problem.setTitle("Credenciais Inválidas");
                problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problem);
        }

        @ExceptionHandler(SenhaAtualIncorretaException.class)
        public ResponseEntity<ProblemDetail> handleSenhaAtualIncorretaException(
                        SenhaAtualIncorretaException ex, WebRequest request) {

                ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                                HttpStatus.UNAUTHORIZED,
                                ex.getMessage());

                problem.setType(URI.create("https://api.techchallenge.com/errors/senha-atual-incorreta"));
                problem.setTitle("Senha Atual Incorreta");
                problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problem);
        }

        @ExceptionHandler(AcessoNegadoException.class)
        public ResponseEntity<ProblemDetail> handleAcessoNegadoException(
                        AcessoNegadoException ex, WebRequest request) {

                ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                                HttpStatus.FORBIDDEN,
                                ex.getMessage());

                problem.setType(URI.create("https://api.techchallenge.com/errors/acesso-negado"));
                problem.setTitle("Acesso Negado");
                problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(problem);
        }

        @ExceptionHandler(EmailJaCadastradoException.class)
        public ResponseEntity<ProblemDetail> handleEmailJaCadastradoException(
                        EmailJaCadastradoException ex, WebRequest request) {

                ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                                HttpStatus.CONFLICT,
                                ex.getMessage());

                problem.setType(URI.create("https://api.techchallenge.com/errors/email-ja-cadastrado"));
                problem.setTitle("Email Já Cadastrado");
                problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));

                return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
        }

        @ExceptionHandler(LoginJaCadastradoException.class)
        public ResponseEntity<ProblemDetail> handleLoginJaCadastradoException(
                        LoginJaCadastradoException ex, WebRequest request) {

                ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                                HttpStatus.CONFLICT,
                                ex.getMessage());

                problem.setType(URI.create("https://api.techchallenge.com/errors/login-ja-cadastrado"));
                problem.setTitle("Login Já Cadastrado");
                problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));

                return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
        }

        @ExceptionHandler(EmailInvalidoException.class)
        public ResponseEntity<ProblemDetail> handleEmailInvalidoException(
                        EmailInvalidoException ex, WebRequest request) {

                ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                                HttpStatus.BAD_REQUEST,
                                ex.getMessage());

                problem.setType(URI.create("https://api.techchallenge.com/errors/email-invalido"));
                problem.setTitle("Email Inválido");
                problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
        }

        @ExceptionHandler(EnderecoInvalidoException.class)
        public ResponseEntity<ProblemDetail> handleEnderecoInvalidoException(
                        EnderecoInvalidoException ex, WebRequest request) {

                ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                                HttpStatus.BAD_REQUEST,
                                ex.getMessage());

                problem.setType(URI.create("https://api.techchallenge.com/errors/endereco-invalido"));
                problem.setTitle("Endereço Inválido");
                problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
        }

        @ExceptionHandler(HorarioInvalidoException.class)
        public ResponseEntity<ProblemDetail> handleHorarioInvalidoException(
                        HorarioInvalidoException ex, WebRequest request) {

                ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                                HttpStatus.BAD_REQUEST,
                                ex.getMessage());

                problem.setType(URI.create("https://api.techchallenge.com/errors/horario-invalido"));
                problem.setTitle("Horário Inválido");
                problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
        }

        @ExceptionHandler(TipoCozinhaInvalidaException.class)
        public ResponseEntity<ProblemDetail> handleTipoCozinhaInvalidaException(
                        TipoCozinhaInvalidaException ex, WebRequest request) {

                ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                                HttpStatus.BAD_REQUEST,
                                ex.getMessage());

                problem.setType(URI.create("https://api.techchallenge.com/errors/tipo-cozinha-invalido"));
                problem.setTitle("Tipo de Cozinha Inválido");
                problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
        }

        @ExceptionHandler(SenhaInvalidaException.class)
        public ResponseEntity<ProblemDetail> handleSenhaInvalidaException(
                        SenhaInvalidaException ex, WebRequest request) {

                ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                                HttpStatus.BAD_REQUEST,
                                ex.getMessage());

                problem.setType(URI.create("https://api.techchallenge.com/errors/senha-invalida"));
                problem.setTitle("Senha Inválida");
                problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
        }

        @ExceptionHandler(SenhasNaoConferemException.class)
        public ResponseEntity<ProblemDetail> handleSenhasNaoConferemException(
                        SenhasNaoConferemException ex, WebRequest request) {

                ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                                HttpStatus.BAD_REQUEST,
                                ex.getMessage());

                problem.setType(URI.create("https://api.techchallenge.com/errors/senhas-nao-conferem"));
                problem.setTitle("Senhas Não Conferem");
                problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
        }

        @ExceptionHandler(CampoObrigatorioException.class)
        public ResponseEntity<ProblemDetail> handleCampoObrigatorioException(
                        CampoObrigatorioException ex, WebRequest request) {

                ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                                HttpStatus.BAD_REQUEST,
                                ex.getMessage());

                problem.setType(URI.create("https://api.techchallenge.com/errors/campo-obrigatorio"));
                problem.setTitle("Campo Obrigatório");
                problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
        }

        @ExceptionHandler(TipoUsuarioInvalidoException.class)
        public ResponseEntity<ProblemDetail> handleTipoUsuarioInvalidoException(
                        TipoUsuarioInvalidoException ex, WebRequest request) {

                ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                                HttpStatus.BAD_REQUEST,
                                ex.getMessage());

                problem.setType(URI.create("https://api.techchallenge.com/errors/tipo-usuario-invalido"));
                problem.setTitle("Tipo de Usuário Inválido");
                problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
        }

        @ExceptionHandler(RestauranteNaoEncontradoException.class)
        public ResponseEntity<ProblemDetail> handleRestauranteNaoEncontradoException(
                        RestauranteNaoEncontradoException ex, WebRequest request) {

                ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                                HttpStatus.NOT_FOUND,
                                ex.getMessage());

                problem.setType(URI.create("https://api.techchallenge.com/errors/restaurante-nao-encontrado"));
                problem.setTitle("Restaurante Não Encontrado");
                problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
        }

        @ExceptionHandler(CardapioItemNaoEncontradoException.class)
        public ResponseEntity<ProblemDetail> handleCardapioItemNaoEncontradoException(
                        CardapioItemNaoEncontradoException ex, WebRequest request) {

                ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                                HttpStatus.NOT_FOUND,
                                ex.getMessage());

                problem.setType(URI.create("https://api.techchallenge.com/errors/cardapio-item-nao-encontrado"));
                problem.setTitle("Item do Cardápio Não Encontrado");
                problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
        }

        @ExceptionHandler(RestauranteComCardapioException.class)
        public ResponseEntity<ProblemDetail> handleRestauranteComCardapioException(
                        RestauranteComCardapioException ex, WebRequest request) {

                ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                                HttpStatus.BAD_REQUEST,
                                ex.getMessage());

                problem.setType(URI.create("https://api.techchallenge.com/errors/restaurante-com-cardapio"));
                problem.setTitle("Restaurante com Cardápio");
                problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
        }

        @ExceptionHandler(DomainException.class)
        public ResponseEntity<ProblemDetail> handleDomainException(
                        DomainException ex, WebRequest request) {

                ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                                HttpStatus.BAD_REQUEST,
                                ex.getMessage());

                problem.setType(URI.create("https://api.techchallenge.com/errors/domain-error"));
                problem.setTitle("Erro de Validação de Negócio");
                problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ProblemDetail> handleValidationException(
                        MethodArgumentNotValidException ex, WebRequest request) {

                Map<String, String> errors = new HashMap<>();
                ex.getBindingResult().getAllErrors().forEach(error -> {
                        String fieldName = ((FieldError) error).getField();
                        String errorMessage = error.getDefaultMessage();
                        errors.put(fieldName, errorMessage);
                });

                ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                                HttpStatus.BAD_REQUEST,
                                "Validação de campos falhou");

                problem.setType(URI.create("https://api.techchallenge.com/errors/validacao-campos"));
                problem.setTitle("Erro de Validação");
                problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
                problem.setProperty("errors", errors);

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ProblemDetail> handleGenericException(
                        Exception ex, WebRequest request) {

                ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "Erro interno do servidor");

                problem.setType(URI.create("https://api.techchallenge.com/errors/internal-server-error"));
                problem.setTitle("Erro Interno");
                problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
        }
}
