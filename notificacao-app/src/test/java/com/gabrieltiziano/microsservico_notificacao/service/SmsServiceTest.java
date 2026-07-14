package com.gabrieltiziano.microsservico_notificacao.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SmsServiceTest {

    @Mock
    private SnsClient snsClient;

    @InjectMocks
    private SmsService smsService;

    @Test
    @DisplayName("Deve publicar no SNS com o telefone e a mensagem informados")
    void devePublicarSmsComTelefoneEMensagem() {
        smsService.enviarSms("+5511999999999", "Sua proposta foi aprovada");

        ArgumentCaptor<PublishRequest> captor = ArgumentCaptor.forClass(PublishRequest.class);
        verify(snsClient).publish(captor.capture());
        PublishRequest enviado = captor.getValue();
        assertThat(enviado.phoneNumber()).isEqualTo("+5511999999999");
        assertThat(enviado.message()).isEqualTo("Sua proposta foi aprovada");
    }
}
