package com.projetoPagSimples.pag.domain.metodo_pagamento.entity;

import com.projetoPagSimples.pag.domain.metodo_pagamento.enums.TipoPagamento;
import com.projetoPagSimples.pag.domain.usuario.entity.UsuarioEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "TB_METODO_PAGAMENTO")

public class Metodo_PagamentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPagamento tipo;

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

    public TipoPagamento getTipo() {
        return tipo;
    }

    public void setTipo(TipoPagamento tipo) {
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
