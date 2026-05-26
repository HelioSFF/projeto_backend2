package com.projeto_backend.ClinMed.domain.paciente.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public class PacienteRequestDTO {

    @NotBlank(message = "Nome do paciente obrigatório.")
    private String nome;

    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 números.")
    private String cpf;

    @NotNull(message = "Data de nascimento do paciente obrigatória.")
    private LocalDate dataNascimento;

    @NotBlank(message = "Email do usuario obrigatório.")
    @Email(message = "Email inválido.")
    private String email;

    @NotBlank(message = "Telefone do paciente obrigatório.")
    private String telefone;


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}