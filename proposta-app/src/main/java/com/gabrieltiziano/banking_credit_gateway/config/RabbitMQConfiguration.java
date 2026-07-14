package com.gabrieltiziano.banking_credit_gateway.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    // Exchanges
    @Value("${rabbitmq.exchange.proposta-pendente}")   private String exchangePropostaPendente;
    @Value("${rabbitmq.exchange.proposta-concluida}")  private String exchangePropostaConcluida;
    @Value("${rabbitmq.exchange.dead-letter}")         private String exchangeDeadLetter;

    // Queues
    @Value("${rabbitmq.queue.analise-credito}")        private String filaAnaliseCredito;
    @Value("${rabbitmq.queue.notificacao-pendente}")   private String filaNotificacaoPendente;
    @Value("${rabbitmq.queue.proposta}")               private String filaProposta;
    @Value("${rabbitmq.queue.notificacao-concluida}")  private String filaNotificacaoConcluida;

    // DLQs
    @Value("${rabbitmq.dlq.analise-credito}")          private String dlqAnaliseCredito;
    @Value("${rabbitmq.dlq.notificacao-pendente}")     private String dlqNotificacaoPendente;
    @Value("${rabbitmq.dlq.proposta}")                 private String dlqProposta;
    @Value("${rabbitmq.dlq.notificacao-concluida}")    private String dlqNotificacaoConcluida;

    // EXCHANGES
    @Bean
    public FanoutExchange criarFanoutExchangePropostaPendente(){
        return ExchangeBuilder.fanoutExchange(exchangePropostaPendente).build();
    }

    @Bean
    public FanoutExchange criarFanoutExchangePropostaConcluida(){
        return ExchangeBuilder.fanoutExchange(exchangePropostaConcluida).build();
    }

    // DLX
    @Bean
    public DirectExchange criarDeadLetterExchange(){
        return ExchangeBuilder.directExchange(exchangeDeadLetter).build();
    }

    // PROPOSTA PENDENTE | MS ANÁLISE CRÉDITO
    @Bean
    public Queue criarFilaPropostaPendenteMsAnaliseCredito(){
        return QueueBuilder.durable(filaAnaliseCredito)
                .deadLetterExchange(exchangeDeadLetter)
                .deadLetterRoutingKey(dlqAnaliseCredito)
                .build();
    }

    @Bean
    public Queue criarDLQPropostaPendenteMsAnaliseCredito(){
        return QueueBuilder.durable(dlqAnaliseCredito).build();
    }

    @Bean
    public Binding criarBindingDLQPropostaPendenteMsAnaliseCredito(){
        return BindingBuilder
                .bind(criarDLQPropostaPendenteMsAnaliseCredito())
                .to(criarDeadLetterExchange())
                .with(dlqAnaliseCredito);
    }

    @Bean
    public Binding criarBindingPropostaPendenteMSAnaliseCredito(){
        return BindingBuilder
                .bind(criarFilaPropostaPendenteMsAnaliseCredito())
                .to(criarFanoutExchangePropostaPendente());
    }

    // 2) PROPOSTA PENDENTE | MS NOTIFICAÇÃO
    @Bean
    public Queue criarFilaPropostaPendenteMsNotificacao(){
        return QueueBuilder.durable(filaNotificacaoPendente)
                .deadLetterExchange(exchangeDeadLetter)
                .deadLetterRoutingKey(dlqNotificacaoPendente)
                .build();
    }

    @Bean
    public Binding criarBindingPropostaPendenteMSNotificacao(){
        return BindingBuilder
                .bind(criarFilaPropostaPendenteMsNotificacao())
                .to(criarFanoutExchangePropostaPendente());
    }

    @Bean
    public Queue criarDLQPropostaPendenteMsNotificacao(){
        return QueueBuilder.durable(dlqNotificacaoPendente).build();
    }

    @Bean
    public Binding criarBindingDLQPropostaPendenteMsNotificacao(){
        return BindingBuilder
                .bind(criarDLQPropostaPendenteMsNotificacao())
                .to(criarDeadLetterExchange())
                .with(dlqNotificacaoPendente);
    }

    // 3) PROPOSTA CONCLUÍDA | MS PROPOSTA
    @Bean
    public Queue criarFilaPropostaConcluidaMsProposta(){
        return QueueBuilder.durable(filaProposta)
                .deadLetterExchange(exchangeDeadLetter)
                .deadLetterRoutingKey(dlqProposta)
                .build();
    }

    @Bean
    public Binding criarBindingPropostaConcluidaMSPropostaApp(){
        return BindingBuilder
                .bind(criarFilaPropostaConcluidaMsProposta())
                .to(criarFanoutExchangePropostaConcluida());
    }

    @Bean
    public Queue criarDLQPropostaConcluidaMsProposta(){
        return QueueBuilder.durable(dlqProposta).build();
    }

    @Bean
    public Binding criarBindingDLQPropostaConcluidaMsProposta(){
        return BindingBuilder
                .bind(criarDLQPropostaConcluidaMsProposta())
                .to(criarDeadLetterExchange())
                .with(dlqProposta);
    }

    // 4) PROPOSTA CONCLUÍDA | MS NOTIFICAÇÃO
    @Bean
    public Queue criarFilaPropostaConcluidaMsNotificacao(){
        return QueueBuilder.durable(filaNotificacaoConcluida)
                .deadLetterExchange(exchangeDeadLetter)
                .deadLetterRoutingKey(dlqNotificacaoConcluida)
                .build();
    }

    @Bean
    public Binding criarBindingPropostaConcluidaMSNotificacao(){
        return BindingBuilder
                .bind(criarFilaPropostaConcluidaMsNotificacao())
                .to(criarFanoutExchangePropostaConcluida());  // fanout CONCLUÍDA
    }

    @Bean
    public Queue criarDLQPropostaConcluidaMsNotificacao(){
        return QueueBuilder.durable(dlqNotificacaoConcluida).build();
    }

    @Bean
    public Binding criarBindingDLQPropostaConcluidaMsNotificacao(){
        return BindingBuilder
                .bind(criarDLQPropostaConcluidaMsNotificacao())
                .to(criarDeadLetterExchange())
                .with(dlqNotificacaoConcluida);
    }

    // OUTROS
    @Bean
    public RabbitAdmin criarRabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> inicializarAdmin(RabbitAdmin rabbitAdmin){
        return event -> rabbitAdmin.initialize();
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

}
