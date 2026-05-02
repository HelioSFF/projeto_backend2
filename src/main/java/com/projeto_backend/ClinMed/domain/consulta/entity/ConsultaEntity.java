package com.projeto_backend.ClinMed.domain.consulta.entity;

import com.projeto_backend.ClinMed.domain.consulta.enums.StatusConsulta;
import com.projeto_backend.ClinMed.domain.medico.entity.MedicoEntity;
import com.projeto_backend.ClinMed.domain.paciente.entity.PacienteEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_CONSULTA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataHoraInicio;

    @Column(nullable = false)
    private LocalDateTime dataHoraFim;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusConsulta status;

    @Column(columnDefinition = "TEXT")
    private String motivo;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private PacienteEntity paciente;

    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false)
    private MedicoEntity medico;
}
