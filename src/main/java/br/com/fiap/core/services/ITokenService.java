package br.com.fiap.core.services;

public interface ITokenService {

    String gerarToken(Long usuarioId, String email, String tipoUsuario);

    Long extrairUsuarioId(String token);

    boolean isTokenValido(String token);
}
