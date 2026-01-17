package br.com.fiap.infra.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tipo_usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipoUsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 50, unique = true)
    private String nome;

    @Column(name = "descricao", nullable = false, length = 200)
    private String descricao;

    public TipoUsuarioEntity(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }
}
