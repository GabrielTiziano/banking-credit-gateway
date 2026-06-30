package com.gabrieltiziano.banking_credit_gateway.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

@Service
public class SmsService {
    private final SnsClient snsClient;

    public SmsService(SnsClient snsClient) {
        this.snsClient = snsClient;
    }

    public void enviarSms(String telefone, String mensagem) {
        PublishRequest request = PublishRequest.builder()
                .phoneNumber(telefone)
                .message(mensagem)
                .build();
        snsClient.publish(request);
    }
}
