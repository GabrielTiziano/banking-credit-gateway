package com.gabrieltiziano.analise_credito.service.strategy.impl;

import com.gabrieltiziano.analise_credito.domain.Proposta;
import com.gabrieltiziano.analise_credito.service.strategy.CalculaPontuacao;

public class PrazoPagamentoInferior5AnosImpl implements CalculaPontuacao {
    @Override
    public int calcular(Proposta proposta) {
        return proposta.getPrazoPagamento() < 60 ? 80 : 0;
    }
}
