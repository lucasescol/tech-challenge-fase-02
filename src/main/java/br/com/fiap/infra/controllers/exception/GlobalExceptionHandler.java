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

import br.com.fiap.core.exceptions.CredenciaisInvalidasException;
import br.com.fiap.core.exceptions.DomainException;
import br.com.fiap.core.exceptions.EmailInvalidoException;
import br.com.fiap.core.exceptions.EnderecoInvalidoException;
import br.com.fiap.core.exceptions.HorarioInvalidoException;
import br.com.fiap.core.exceptions.SenhaInvalidaException;
import br.com.fiap.core.exceptions.SenhasNaoConferemException;
import br.com.fiap.core.exceptions.TipoCozinhaInvalidaException;
import br.com.fiap.core.exceptions.UsuarioNaoEncontradoException;

@RestControllerAdvice
public class GlobalExceptionHandler {

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

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ProblemDetail> handleValidationException(
                        MethodArgumentNotValidException ex, WebRequest request) {

                Map<String, String> errors = new HashMap<>();
                ex.getBindingResult().getAllErrors().forEach((error) -> {
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
