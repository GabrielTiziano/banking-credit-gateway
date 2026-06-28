package com.gabrieltiziano.banking_credit_gateway.agendador;

import com.gabrieltiziano.banking_credit_gateway.dto.PropostaResponse;
import com.gabrieltiziano.banking_credit_gateway.entities.Proposta;
import com.gabrieltiziano.banking_credit_gateway.repository.PropostaRepository;
import com.gabrieltiziano.banking_credit_gateway.service.NotificacaoService;
import org.springframework.beans.factory.annotation.Value;

public class PropostaSemIntegracao {
    private final PropostaRepository propostaRepository;
    private final String exchange;
    private final NotificacaoService notificacaoService;

    public PropostaSemIntegracao(PropostaRepository propostaRepository,
                                 @Value("${rabbitmq.propostapendente.exchange}") String exchange, NotificacaoService notificacaoService) {
        this.propostaRepository = propostaRepository;
        this.exchange = exchange;
        this.notificacaoService = notificacaoService;
    }

    public void buscarPropostasSemIntegracao(){
        propostaRepository.findAllByIntegradaIsFalse().forEach(proposta -> {
            try {
                notificacaoService.notificar(PropostaResponse.from(proposta), exchange);
                atualizarProposta(proposta);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void atualizarProposta(Proposta proposta){
        proposta.setIntegrada(true);
        propostaRepository.save(proposta);
    }
}
