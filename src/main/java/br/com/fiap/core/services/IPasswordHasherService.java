package br.com.fiap.core.services;

public interface IPasswordHasherService {
    boolean matches(String plainPassword, String hash);
    String encode(String plainPassword);
}
