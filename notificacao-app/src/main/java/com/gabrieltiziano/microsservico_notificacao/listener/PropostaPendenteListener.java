package com.gabrieltiziano.microsservico_notificacao.listener;

import com.gabrieltiziano.microsservico_notificacao.constante.MensagemConstante;
import com.gabrieltiziano.microsservico_notificacao.domain.Proposta;
import com.gabrieltiziano.microsservico_notificacao.service.SmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PropostaPendenteListener {
    private final SmsService smsService;

    public PropostaPendenteListener(SmsService smsService) {
        this.smsService = smsService;
    }

    @RabbitListener(queues = "${rabbitmq.queue.proposta.pendente}")
    public void propostaPendente(Proposta proposta){
        String mensagem = String.format(
                MensagemConstante.PROPOSTA_EM_ANALISE,
                proposta.getUsuario().getNome(),
                proposta.getValorSolicitado(),
                proposta.getPrazoPagamento());

        smsService.enviarSms(proposta.getUsuario().getTelefone(), mensagem);
    }
}
