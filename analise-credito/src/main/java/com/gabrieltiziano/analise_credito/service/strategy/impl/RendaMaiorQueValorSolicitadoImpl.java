package com.gabrieltiziano.analise_credito.service.strategy.impl;

import com.gabrieltiziano.analise_credito.domain.Proposta;
import com.gabrieltiziano.analise_credito.service.strategy.CalculaPontuacao;

import java.math.BigDecimal;

public class RendaMaiorQueValorSolicitadoImpl implements CalculaPontuacao {
    @Override
    public int calcular(Proposta proposta) {
        return rendaMaiorQueValorSolicitado(proposta) ? 100 : 0;
    }

    private boolean rendaMaiorQueValorSolicitado(Proposta proposta){
        return proposta.getUsuario().getRenda()
                .compareTo(proposta.getValorSolicitado()) > 0;
    }
}
