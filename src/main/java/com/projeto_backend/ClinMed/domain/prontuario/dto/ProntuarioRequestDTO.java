package com.projeto_backend.ClinMed.domain.prontuario.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProntuarioRequestDTO {

    @NotBlank(message = "Sintomas são obrigatórios.")
    private String sintomas;

    @NotBlank(message = "Diagnóstico é obrigatório.")
    private String diagnostico;

    @NotBlank(message = "Prescrição é obrigatória.")
    private String prescricao;

    private String observacoesPrivadas;

    @NotNull(message = "Paciente é obrigatório.")
    private Long pacienteId;

    @NotNull(message = "Consulta é obrigatória.")
    private Long consultaId;

    public String getSintomas() {
        return sintomas;
    }

    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getPrescricao() {
        return prescricao;
    }

    public void setPrescricao(String prescricao) {
        this.prescricao = prescricao;
    }

    public String getObservacoesPrivadas() {
        return observacoesPrivadas;
    }

    public void setObservacoesPrivadas(String observacoesPrivadas) {
        this.observacoesPrivadas = observacoesPrivadas;
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }

    public Long getConsultaId() {
        return consultaId;
    }

    public void setConsultaId(Long consultaId) {
        this.consultaId = consultaId;
    }
}
