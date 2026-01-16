package br.com.fiap.core.domain;

import br.com.fiap.core.exceptions.DomainException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Restaurante {
    private final Long id;
    private final String nome;
    private final Endereco endereco;
    private final TipoCozinha tipoCozinha;
    private final HorarioFuncionamento horarioFuncionamento;
    private final Long donoRestaurante;

    private Restaurante(Long id, String nome, Endereco endereco, TipoCozinha tipoCozinha,
            HorarioFuncionamento horarioFuncionamento, Long donoRestaurante) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.tipoCozinha = tipoCozinha;
        this.horarioFuncionamento = horarioFuncionamento;
        this.donoRestaurante = donoRestaurante;
    }

    public static Restaurante create(Long id, String nome, String logradouro, String numero,
            String complemento, String bairro, String cidade, String estado,
            String cep, String tipoCozinha, String horarioFuncionamento,
            Long donoRestaurante) {

        if (nome == null || nome.trim().isEmpty()) {
            throw new DomainException("Nome do restaurante não pode ser vazio");
        }

        Endereco endereco = new Endereco(logradouro, numero, complemento, bairro, cidade, estado, cep);
        TipoCozinha tipo = new TipoCozinha(tipoCozinha);
        HorarioFuncionamento horario = new HorarioFuncionamento(horarioFuncionamento);

        return new Restaurante(id, nome.trim(), endereco, tipo, horario, donoRestaurante);
    }
}
