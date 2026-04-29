package com.projeto_backend.ClinMed.domain.consulta.entity;


import com.projeto_backend.ClinMed.domain.paciente.entity.PacienteEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_TRANSACAO")
public class ConsultaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataTransacao;

    @OneToOne
    @JoinColumn(name = "id_pagamento")
    private PacienteEntity pagamento;


}
