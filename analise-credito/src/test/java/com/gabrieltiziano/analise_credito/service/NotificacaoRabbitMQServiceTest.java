package com.gabrieltiziano.analise_credito.service;

import com.gabrieltiziano.analise_credito.domain.Proposta;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificacaoRabbitMQServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private NotificacaoRabbitMQService service;

    @Test
    @DisplayName("Deve publicar a proposta na exchange informada com routing key vazia")
    void devePublicarNaExchange() {
        Proposta proposta = new Proposta();

        service.notificar("proposta-concluida.ex", proposta);

        verify(rabbitTemplate).convertAndSend(eq("proposta-concluida.ex"), eq(""), eq(proposta));
    }
}
