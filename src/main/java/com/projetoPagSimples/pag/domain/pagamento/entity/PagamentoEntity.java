package com.projetoPagSimples.pag.domain.pagamento.entity;

import com.projetoPagSimples.pag.domain.metodo_pagamento.entity.Metodo_PagamentoEntity;
import com.projetoPagSimples.pag.domain.pagamento.enums.StatusPagamento;
import com.projetoPagSimples.pag.domain.transacao.entity.TransacaoEntity;
import com.projetoPagSimples.pag.domain.usuario.entity.UsuarioEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="TB_PAGAMENTO")
public class PagamentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double valor;

    @Column(nullable = false)
    private LocalDateTime data;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioEntity usuario;

    @OneToOne
    @JoinColumn(name = "id_transacao", nullable = false)
    private TransacaoEntity transacao;

    @ManyToOne
    @JoinColumn(name = "id_metodo_pagamento", nullable = false)
    private Metodo_PagamentoEntity metodoPagamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPagamento status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

    public TransacaoEntity getTransacao() {
        return transacao;
    }

    public void setTransacao(TransacaoEntity transacao) {
        this.transacao = transacao;
    }

    public Metodo_PagamentoEntity getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(Metodo_PagamentoEntity metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }
}
