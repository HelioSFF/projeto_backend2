package com.projetoPagSimples.pag.domain.transacao.entity;


import com.projetoPagSimples.pag.domain.pagamento.entity.PagamentoEntity;
import jakarta.persistence.*;

import javax.annotation.processing.Generated;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_TRANSACAO")
public class TransacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataTransacao;

    @OneToOne
    @JoinColumn(name = "id_pagamento")
    private PagamentoEntity pagamento;


}
