package com.projeto_backend.ClinMed.domain.especialidade.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_ESPECIALIDADE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EspecialidadeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    // Relacionamento @OneToMany com MedicoEntity deve ser implementado 
    // quando a classe MedicoEntity for refatorada para incluir o @ManyToOne(name="especialidade")
    // private List<MedicoEntity> medicos;
}
