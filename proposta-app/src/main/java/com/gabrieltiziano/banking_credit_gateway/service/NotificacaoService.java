package com.gabrieltiziano.banking_credit_gateway.service;

import com.gabrieltiziano.banking_credit_gateway.dto.PropostaResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoService {
    private final RabbitTemplate rabbitTemplate;

    public NotificacaoService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void notificar(PropostaResponse dto, String exchange){
        rabbitTemplate.convertAndSend(exchange, "", dto);
    }
}
