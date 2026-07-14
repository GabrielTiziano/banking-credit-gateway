package com.gabrieltiziano.banking_credit_gateway.listener;

import com.gabrieltiziano.banking_credit_gateway.dto.PropostaConcluidaMessage;
import com.gabrieltiziano.banking_credit_gateway.dto.PropostaResponse;
import com.gabrieltiziano.banking_credit_gateway.entities.Proposta;
import com.gabrieltiziano.banking_credit_gateway.repository.PropostaRepository;
import com.gabrieltiziano.banking_credit_gateway.service.WebSocketService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PropostaConcluidaListener {
    private final PropostaRepository propostaRepository;
    private final WebSocketService webSocketService;

    public PropostaConcluidaListener(PropostaRepository propostaRepository, WebSocketService webSocketService) {
        this.propostaRepository = propostaRepository;
        this.webSocketService = webSocketService;
    }

    @RabbitListener(queues = "${rabbitmq.queue.proposta}")
    public void propostaConcluida(PropostaConcluidaMessage proposta) {
        Proposta existente = propostaRepository.findById(proposta.id()).orElseThrow();

        existente.setStatus(proposta.status());
        existente.setObservacao(proposta.observacao());

        propostaRepository.save(existente);
        webSocketService.notificar(PropostaResponse.from(existente));
    }
}
