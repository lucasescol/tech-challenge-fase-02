package br.com.fiap.core.services;

public interface IPasswordHasherService {

    String hash(String plainPassword);

    boolean verify(String plainPassword, String hashedPassword);
}
