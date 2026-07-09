package com.gabrieltiziano.analise_credito.service.strategy.impl;

import com.gabrieltiziano.analise_credito.domain.Proposta;
import com.gabrieltiziano.analise_credito.service.strategy.CalculaPontuacao;

import java.util.Random;

public class OutrosEmprestimosEmAndamentoImpl implements CalculaPontuacao {
    @Override
    public int calcular(Proposta proposta) {
        return outrosEmprestimosEmAndamento() ? 0 : 80;
    }

    //simulacao de chamada ao BACEN
    private boolean outrosEmprestimosEmAndamento(){
        return new Random().nextBoolean();
    }
}
