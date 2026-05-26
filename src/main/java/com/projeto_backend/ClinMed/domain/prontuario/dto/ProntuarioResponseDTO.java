package com.projeto_backend.ClinMed.domain.prontuario.dto;

public class ProntuarioResponseDTO {

    private String sintomas;
    private String diagnostico;
    private String prescricao;
    private String observacoesPrivadas;
    private Long pacienteId;
    private Long consultaId;

    public ProntuarioResponseDTO(String sintomas, String diagnostico, String prescricao, String observacoesPrivadas, Long pacienteId, Long consultaId) {
        this.sintomas = sintomas;
        this.diagnostico = diagnostico;
        this.prescricao = prescricao;
        this.observacoesPrivadas = observacoesPrivadas;
        this.pacienteId = pacienteId;
        this.consultaId = consultaId;
    }

    public String getSintomas() {
        return sintomas;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public String getPrescricao() {
        return prescricao;
    }

    public String getObservacoesPrivadas() {
        return observacoesPrivadas;
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public Long getConsultaId() {
        return consultaId;
    }

}
