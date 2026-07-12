package com.gabrieltiziano.banking_credit_gateway.service;

import com.gabrieltiziano.banking_credit_gateway.dto.PropostaResponse;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public WebSocketService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void notificar(PropostaResponse propostaResponse) {
        simpMessagingTemplate.convertAndSend("/propostas", propostaResponse);
    }
}
