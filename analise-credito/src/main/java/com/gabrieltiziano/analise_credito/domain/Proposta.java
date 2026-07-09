package com.gabrieltiziano.analise_credito.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Proposta {
    private Long id;

    private BigDecimal valorSolicitado;

    private Integer prazoPagamento;

    private StatusProposta status = StatusProposta.EM_ANALISE;

    private String observacao;

    private Usuario usuario;
}
