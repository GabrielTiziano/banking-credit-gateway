package com.gabrieltiziano.analise_credito.service;

import com.gabrieltiziano.analise_credito.domain.Proposta;
import com.gabrieltiziano.analise_credito.domain.StatusProposta;
import com.gabrieltiziano.analise_credito.exceptions.StrategyException;
import com.gabrieltiziano.analise_credito.service.strategy.CalculaPontuacao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnaliseCreditoServiceTest {

    private static final String EXCHANGE = "proposta-concluida.ex";

    @Mock
    private CalculaPontuacao strategyA;

    @Mock
    private CalculaPontuacao strategyB;

    @Mock
    private NotificacaoRabbitMQService notificacaoRabbitMQService;

    private AnaliseCreditoService criarService(List<CalculaPontuacao> strategies) {
        return new AnaliseCreditoService(strategies, notificacaoRabbitMQService, EXCHANGE);
    }

    @Test
    @DisplayName("Soma das pontuações acima de 350 deve APROVAR a proposta")
    void deveAprovarQuandoSomaMaiorQue350() {
        when(strategyA.calcular(any())).thenReturn(200);
        when(strategyB.calcular(any())).thenReturn(200); // total 400 > 350
        AnaliseCreditoService service = criarService(List.of(strategyA, strategyB));
        Proposta proposta = new Proposta();

        service.analisar(proposta);

        assertThat(proposta.getStatus()).isEqualTo(StatusProposta.APROVADA);
        verify(notificacaoRabbitMQService).notificar(eq(EXCHANGE), eq(proposta));
    }

    @Test
    @DisplayName("Soma das pontuações igual ou abaixo de 350 deve REJEITAR a proposta")
    void deveRejeitarQuandoSomaMenorOuIgualA350() {
        when(strategyA.calcular(any())).thenReturn(100);
        when(strategyB.calcular(any())).thenReturn(100); // total 200 <= 350
        AnaliseCreditoService service = criarService(List.of(strategyA, strategyB));
        Proposta proposta = new Proposta();

        service.analisar(proposta);

        assertThat(proposta.getStatus()).isEqualTo(StatusProposta.REJEITADA);
        verify(notificacaoRabbitMQService).notificar(eq(EXCHANGE), eq(proposta));
    }

    @Test
    @DisplayName("StrategyException deve REJEITAR e gravar o motivo na observação")
    void deveRejeitarComObservacaoQuandoStrategyLancaExcecao() {
        when(strategyA.calcular(any())).thenThrow(new StrategyException("CPF negativado"));
        AnaliseCreditoService service = criarService(List.of(strategyA));
        Proposta proposta = new Proposta();

        service.analisar(proposta);

        assertThat(proposta.getStatus()).isEqualTo(StatusProposta.REJEITADA);
        assertThat(proposta.getObservacao()).isEqualTo("CPF negativado");
        verify(notificacaoRabbitMQService).notificar(eq(EXCHANGE), eq(proposta));
    }

    @Test
    @DisplayName("Independente do resultado, sempre notifica o RabbitMQ uma vez")
    void deveNotificarSempreUmaVez() {
        when(strategyA.calcular(any())).thenReturn(500);
        AnaliseCreditoService service = criarService(List.of(strategyA));

        service.analisar(new Proposta());

        verify(notificacaoRabbitMQService, times(1)).notificar(eq(EXCHANGE), any(Proposta.class));
    }
}
