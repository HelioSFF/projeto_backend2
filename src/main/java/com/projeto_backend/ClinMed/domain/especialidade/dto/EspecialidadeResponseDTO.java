package com.projeto_backend.ClinMed.domain.especialidade.dto;

public class EspecialidadeResponseDTO {

    private Long id;
    private String nome;
    private String descricao;

    public EspecialidadeResponseDTO(Long id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }
}
