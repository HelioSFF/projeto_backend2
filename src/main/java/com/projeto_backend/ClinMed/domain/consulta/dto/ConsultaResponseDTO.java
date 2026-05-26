package com.projeto_backend.ClinMed.domain.consulta.dto;

import com.projeto_backend.ClinMed.domain.consulta.enums.StatusConsulta;
import java.time.LocalDateTime;

public class ConsultaResponseDTO {

    private LocalDateTime dataHoraInicio;
    private String motivo;
    private String pacienteNome;
    private String medicoNome;

    public ConsultaResponseDTO(LocalDateTime dataHoraInicio, String motivo, String pacienteNome, String medicoNome) {

        this.dataHoraInicio = dataHoraInicio;
        this.motivo = motivo;
        this.pacienteNome = pacienteNome;
        this.medicoNome = medicoNome;
    }

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
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
