package br.com.fiap.core.services;

public interface ITokenService {
    String generateToken(String userId, String role);
}
