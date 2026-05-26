package com.projeto_backend.ClinMed.domain.medico.entity;

import com.projeto_backend.ClinMed.domain.especialidade.entity.EspecialidadeEntity;
import com.projeto_backend.ClinMed.domain.usuario.entity.UsuarioEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "TB_MEDICO")
public class MedicoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "especialidade_id", nullable = false)
    private EspecialidadeEntity especialidade;

    @Column(name="detalhes")
    private String detalhes;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private UsuarioEntity usuario;

    public MedicoEntity(EspecialidadeEntity especialidade, String detalhes, UsuarioEntity usuario) {
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

    public EspecialidadeEntity getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(EspecialidadeEntity especialidade) {
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
