package com.gabrieltiziano.microsservico_notificacao.listener;

import com.gabrieltiziano.microsservico_notificacao.constante.MensagemConstante;
import com.gabrieltiziano.microsservico_notificacao.domain.Proposta;
import com.gabrieltiziano.microsservico_notificacao.domain.StatusProposta;
import com.gabrieltiziano.microsservico_notificacao.service.SmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AnaliseConcluidaListener {
    private final SmsService smsService;

    public AnaliseConcluidaListener(SmsService smsService) {
        this.smsService = smsService;
    }

    @RabbitListener(queues = "${rabbitmq.queue.proposta.concluida}")
    public void analiseConcluida(Proposta proposta) {
        if (proposta.getUsuario() == null || proposta.getUsuario().getTelefone() == null) {
            return;
        }

        if (proposta.getStatus() == StatusProposta.APROVADA) {
            String mensagemAprovada = String.format(
                    MensagemConstante.PROPOSTA_APROVADA,
                    proposta.getUsuario().getNome());
            smsService.enviarSms(proposta.getUsuario().getTelefone(), mensagemAprovada);
        } else {
            String mensagemReprovada = String.format(
                    MensagemConstante.PROPOSTA_REJEITADA,
                    proposta.getUsuario().getNome(),
                    proposta.getObservacao());
            smsService.enviarSms(proposta.getUsuario().getTelefone(), mensagemReprovada);
        }
    }
}
