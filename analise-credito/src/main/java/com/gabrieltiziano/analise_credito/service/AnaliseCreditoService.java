package com.gabrieltiziano.analise_credito.service;

import com.gabrieltiziano.analise_credito.domain.Proposta;
import com.gabrieltiziano.analise_credito.domain.StatusProposta;
import com.gabrieltiziano.analise_credito.exceptions.StrategyException;
import com.gabrieltiziano.analise_credito.service.strategy.CalculaPontuacao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnaliseCreditoService {
    private final List<CalculaPontuacao> strategiesCalculaPontuacao;

    public AnaliseCreditoService(List<CalculaPontuacao> strategiesCalculaPontuacao) {
        this.strategiesCalculaPontuacao = strategiesCalculaPontuacao;
    }

    public void analisar(Proposta proposta) {
        try {
            boolean aprovada = strategiesCalculaPontuacao.stream()
                    .mapToInt(impl -> impl.calcular(proposta))
                    .sum() > 350;
            proposta.setStatus(aprovada ? StatusProposta.APROVADA : StatusProposta.REJEITADA);
        } catch (StrategyException e) {
            proposta.setStatus(StatusProposta.REJEITADA);
        }
    }
}
