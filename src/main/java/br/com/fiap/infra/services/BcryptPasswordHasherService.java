package br.com.fiap.infra.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import br.com.fiap.core.services.IPasswordHasherService;

@Service
public class BcryptPasswordHasherService implements IPasswordHasherService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public BcryptPasswordHasherService() {
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public String hash(String plainPassword) {
        return bCryptPasswordEncoder.encode(plainPassword);
    }

    @Override
    public boolean verify(String plainPassword, String hashedPassword) {
        return bCryptPasswordEncoder.matches(plainPassword, hashedPassword);
    }
}
