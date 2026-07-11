package com.gabrieltiziano.analise_credito.service;

import com.gabrieltiziano.analise_credito.domain.Proposta;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoRabbitMQService {

    private final RabbitTemplate rabbitTemplate;

    public NotificacaoRabbitMQService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void notificar(String exchange, Proposta proposta) {
        rabbitTemplate.convertAndSend(
                exchange,
                "",
                proposta);
    }
}
