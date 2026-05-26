package com.projeto_backend.ClinMed.domain.usuario.dto;

public class UsuarioResponseDTO {

    private String nome;
    private String email;

    public UsuarioResponseDTO(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }
}
