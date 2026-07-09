package com.gabrieltiziano.analise_credito.service.strategy;

import com.gabrieltiziano.analise_credito.domain.Proposta;

public interface CalculaPontuacao {
    int calcular(Proposta proposta);
}
