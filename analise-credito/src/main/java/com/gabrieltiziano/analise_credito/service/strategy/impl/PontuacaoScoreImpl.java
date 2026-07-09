package com.gabrieltiziano.analise_credito.service.strategy.impl;

import com.gabrieltiziano.analise_credito.domain.Proposta;
import com.gabrieltiziano.analise_credito.service.strategy.CalculaPontuacao;

import java.util.Random;

public class PontuacaoScoreImpl implements CalculaPontuacao {
    private final int PONTUACAO_MINIMA = 200;

    @Override
    public int calcular(Proposta proposta) {
        int score = score();

        if (score < PONTUACAO_MINIMA){
            throw new RuntimeException("Pontuação insuficiente para aprovação de crédito.");
        } else if (score <= 400) {
            return 150;
        } else if (score <= 600) {
            return 180;
        } else {
            return 220;
        }

    }

    private int score(){
        return new Random().nextInt(0, 1000);
    }
}
