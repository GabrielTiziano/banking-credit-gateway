package com.gabrieltiziano.analise_credito.service.strategy.impl;

import com.gabrieltiziano.analise_credito.domain.Proposta;
import com.gabrieltiziano.analise_credito.service.strategy.CalculaPontuacao;

import java.util.Random;

public class NomeNegativadoImpl implements CalculaPontuacao {
    @Override
    public int calcular(Proposta proposta) {
        if (cpfNegativado()){
            throw new RuntimeException("CPF negativado, não é possível realizar a análise de crédito.");
        }
        return 100;
    }

    private boolean cpfNegativado(){
        return new Random().nextBoolean();
    }
}
