package com.gabrieltiziano.analise_credito.listener;

import com.gabrieltiziano.analise_credito.domain.Proposta;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PropostaEmAnaliseListener {
    @RabbitListener(queues = "${rabbitmq.queue.proposta.analise}")
    public void propostaEmAnalise(Proposta proposta){

    }
}
