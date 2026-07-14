package com.gabrieltiziano.analise_credito.service.strategy.impl;

import com.gabrieltiziano.analise_credito.domain.Proposta;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PrazoPagamentoInferior5AnosImplTest {

    private final PrazoPagamentoInferior5AnosImpl strategy = new PrazoPagamentoInferior5AnosImpl();

    @Test
    @DisplayName("Prazo menor que 60 meses pontua 80")
    void prazoMenorQue60Pontua80() {
        Proposta proposta = new Proposta();
        proposta.setPrazoPagamento(59);

        assertThat(strategy.calcular(proposta)).isEqualTo(80);
    }

    @Test
    @DisplayName("Prazo de 60 meses ou mais pontua 0")
    void prazoMaiorOuIgual60Pontua0() {
        Proposta proposta = new Proposta();
        proposta.setPrazoPagamento(60);

        assertThat(strategy.calcular(proposta)).isEqualTo(0);
    }
}
