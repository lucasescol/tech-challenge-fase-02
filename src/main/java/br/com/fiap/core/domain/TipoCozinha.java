package br.com.fiap.core.domain;

import br.com.fiap.core.exceptions.TipoCozinhaInvalidaException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@EqualsAndHashCode
public class TipoCozinha {

    private static final List<String> TIPOS_VALIDOS = Arrays.asList(
            "ITALIANA", "JAPONESA", "BRASILEIRA", "CHINESA", "FRANCESA",
            "MEXICANA", "INDIANA", "ARABE", "VEGETARIANA", "VEGANA", "OUTRAS");

    private final String valor;

    public TipoCozinha(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new TipoCozinhaInvalidaException("Tipo de cozinha não pode ser vazio");
        }

        String tipoNormalizado = valor.trim().toUpperCase();

        if (!TIPOS_VALIDOS.contains(tipoNormalizado)) {
            throw new TipoCozinhaInvalidaException(
                    "Tipo de cozinha inválido: " + valor + ". Tipos válidos: " + TIPOS_VALIDOS);
        }

        this.valor = tipoNormalizado;
    }

    @Override
    public String toString() {
        return valor;
    }
}
