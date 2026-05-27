package com.projeto_backend.ClinMed.domain.consulta.dto;

import com.projeto_backend.ClinMed.domain.consulta.enums.StatusConsulta;
import java.time.LocalDateTime;

public class ConsultaResponseDTO {

    private Long id;
    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;
    private StatusConsulta status;
    private String motivo;
    private String pacienteNome;
    private String medicoNome;

    public ConsultaResponseDTO(Long id, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim, StatusConsulta status, String motivo, String pacienteNome, String medicoNome) {
        this.id = id;
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
        this.status = status;
        this.motivo = motivo;
        this.pacienteNome = pacienteNome;
        this.medicoNome = medicoNome;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public LocalDateTime getDataHoraFim() {
        return dataHoraFim;
    }

    public StatusConsulta getStatus() {
        return status;
    }

    public String getMotivo() {
        return motivo;
    }

    public String getPacienteNome() {
        return pacienteNome;
    }

    public String getMedicoNome() {
        return medicoNome;
    }
}
