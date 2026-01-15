package br.com.fiap.infra.services;

import br.com.fiap.core.services.IPasswordHasherService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BCryptPasswordHasherService implements IPasswordHasherService {

    private final PasswordEncoder passwordEncoder;

    public BCryptPasswordHasherService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean matches(String plainPassword, String hash) {
        return passwordEncoder.matches(plainPassword, hash);
    }

    @Override
    public String encode(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }
}
