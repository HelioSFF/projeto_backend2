package com.projeto_backend.ClinMed.domain.especialidade.dto;

import jakarta.validation.constraints.NotBlank;

public class EspecialidadeRequestDTO {

    @NotBlank(message = "Nome da especialidade obrigatório.")
    private String nome;

    @NotBlank(message = "Descrição da especialidade obrigatória")
    private String descricao;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
