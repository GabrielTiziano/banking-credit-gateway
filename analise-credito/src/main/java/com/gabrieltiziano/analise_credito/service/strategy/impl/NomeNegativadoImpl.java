package com.gabrieltiziano.analise_credito.service.strategy.impl;

import com.gabrieltiziano.analise_credito.domain.Proposta;
import com.gabrieltiziano.analise_credito.exceptions.StrategyException;
import com.gabrieltiziano.analise_credito.service.strategy.CalculaPontuacao;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Random;

@Order(1)
@Component
public class NomeNegativadoImpl implements CalculaPontuacao {
    @Override
    public int calcular(Proposta proposta) {
        if (cpfNegativado()){
            throw new StrategyException("CPF negativado, não é possível realizar a análise de crédito.");
        }
        return 100;
    }

    private boolean cpfNegativado(){
        return new Random().nextBoolean();
    }
}
