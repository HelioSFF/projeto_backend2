package com.projeto_backend.ClinMed.domain.medico.dto;

public class MedicoResponseDTO {


    private Long especialidadeId;
    private String detalhes;
    private Long usuarioId;
    private String nomeUsuario;

    public MedicoResponseDTO(Long especialidadeId, String detalhes, Long usuarioId, String nomeUsuario) {
        this.especialidadeId = especialidadeId;
        this.detalhes = detalhes;
        this.usuarioId = usuarioId;
        this.nomeUsuario = nomeUsuario;
    }

    public Long getEspecialidadeId() {
        return especialidadeId;
    }

    public String getDetalhes() {
        return detalhes;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }
}
