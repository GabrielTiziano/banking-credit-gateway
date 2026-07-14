package com.gabrieltiziano.banking_credit_gateway.agendador;

import com.gabrieltiziano.banking_credit_gateway.dto.PropostaPendenteMessage;
import com.gabrieltiziano.banking_credit_gateway.entities.Proposta;
import com.gabrieltiziano.banking_credit_gateway.repository.PropostaRepository;
import com.gabrieltiziano.banking_credit_gateway.service.NotificacaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class PropostaSemIntegracao {
    private final PropostaRepository propostaRepository;
    private final String exchange;
    private final NotificacaoService notificacaoService;

    public PropostaSemIntegracao(PropostaRepository propostaRepository,
                                 @Value("${rabbitmq.exchange.proposta-pendente}") String exchange,
                                 NotificacaoService notificacaoService) {
        this.propostaRepository = propostaRepository;
        this.exchange = exchange;
        this.notificacaoService = notificacaoService;
    }

    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    public void buscarPropostasSemIntegracao() {
        propostaRepository.findAllByIntegradaIsFalse().forEach(proposta -> {
            try {
                notificacaoService.notificar(PropostaPendenteMessage.from(proposta), exchange);
                atualizarProposta(proposta);
            } catch (Exception e) {
                log.warn("Falha ao reenviar proposta {}: {}", proposta.getId(), e.getMessage());
            }
        });
    }

    private void atualizarProposta(Proposta proposta) {
        proposta.setIntegrada(true);
        propostaRepository.save(proposta);
    }
}

