package com.projeto_backend.ClinMed.domain.medico.entity;

import com.projeto_backend.ClinMed.domain.medico.enums.EspecialidadeMedica;
import com.projeto_backend.ClinMed.domain.usuario.entity.UsuarioEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "TB_MEDICO")

public class MedicoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EspecialidadeMedica especialidade;

    @Column(name="detalhes")
    private String detalhes;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private UsuarioEntity usuario;

    public MedicoEntity(EspecialidadeMedica especialidade, String detalhes, UsuarioEntity usuario) {
        this.especialidade = especialidade;
        this.detalhes = detalhes;
        this.usuario = usuario;
    }

    public MedicoEntity(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EspecialidadeMedica getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(EspecialidadeMedica especialidade) {
        this.especialidade = especialidade;
    }

    public String getDetalhes() {
        return detalhes;
    }

    public void setDetalhes(String detalhes) {
        this.detalhes = detalhes;
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }
}
