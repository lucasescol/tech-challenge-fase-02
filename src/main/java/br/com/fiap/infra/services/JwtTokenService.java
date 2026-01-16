package br.com.fiap.infra.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import br.com.fiap.core.services.ITokenService;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtTokenService implements ITokenService {

    @Value("${app.jwt.secret:your-secret-key-that-should-be-at-least-32-characters-long}")
    private String jwtSecret;

    @Value("${app.jwt.expiration:86400000}")
    private long jwtExpirationMs;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    @Override
    public String gerarToken(Long usuarioId, String email, String tipoUsuario) {
        Date agora = new Date();
        Date dataExpiracao = new Date(agora.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .subject(usuarioId.toString())
                .claim("email", email)
                .claim("tipoUsuario", tipoUsuario)
                .issuedAt(agora)
                .expiration(dataExpiracao)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    @Override
    public Long extrairUsuarioId(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return Long.parseLong(claims.getSubject());
        } catch (Exception e) {
            throw new IllegalArgumentException("Token inválido ou expirado", e);
        }
    }

    @Override
    public boolean isTokenValido(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
