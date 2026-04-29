package com.projeto_backend.ClinMed.domain.paciente.entity;

import com.projeto_backend.ClinMed.domain.medico.entity.MedicoEntity;
import com.projeto_backend.ClinMed.domain.paciente.enums.StatusPaciente;
import com.projeto_backend.ClinMed.domain.consulta.entity.ConsultaEntity;
import com.projeto_backend.ClinMed.domain.usuario.entity.UsuarioEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="TB_PACIENTE")
public class PacienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    }
}
