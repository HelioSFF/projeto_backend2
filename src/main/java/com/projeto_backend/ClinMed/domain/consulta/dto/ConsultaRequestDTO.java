package com.projeto_backend.ClinMed.domain.consulta.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class ConsultaRequestDTO {

    @NotNull(message = "A data e hora de inicio sao obrigatórias.")
    @Future(message = "A consulta deve ser agendada para uma data futura.")
    private LocalDateTime dataHoraInicio;

    @Size(max = 255, message = "O motivo deve ter no máximo 255 caracteres.")
    private String motivo;

    @NotNull(message = "O paciente é obrigatório.")
    private Long pacienteId;

    @NotNull(message = "O médico é obrigatório.")
    private Long medicoId;

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }

    public Long getMedicoId() {
        return medicoId;
    }

    public void setMedicoId(Long medicoId) {
        this.medicoId = medicoId;
    }
}
