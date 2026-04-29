package com.projeto_backend.ClinMed.domain.medico.entity;

import com.projeto_backend.ClinMed.domain.medico.enums.EspecialidadeMedica;
import com.projeto_backend.ClinMed.domain.usuario.entity.UsuarioEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "TB_METODO_PAGAMENTO")

public class MedicoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EspecialidadeMedica tipo;

    @Column(name="detalhes")
    private String detalhes;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private UsuarioEntity usuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EspecialidadeMedica getTipo() {
        return tipo;
    }

    public void setTipo(EspecialidadeMedica tipo) {
        this.tipo = tipo;
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
