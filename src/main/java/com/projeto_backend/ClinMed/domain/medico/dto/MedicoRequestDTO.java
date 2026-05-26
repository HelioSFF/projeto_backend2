package com.projeto_backend.ClinMed.domain.medico.dto;

import jakarta.validation.constraints.NotNull;

public class MedicoRequestDTO {

    @NotNull(message = "Especialidade é obrigatória.")
    private Long especialidadeId;

    private String detalhes;

    @NotNull(message = "Usuario é obrigatório.")
    private Long usuarioId;

    public Long getEspecialidadeId() {
        return especialidadeId;
    }

    public void setEspecialidadeId(Long especialidadeId) {
        this.especialidadeId = especialidadeId;
    }

    public String getDetalhes() {
        return detalhes;
    }

    public void setDetalhes(String detalhes) {
        this.detalhes = detalhes;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}
