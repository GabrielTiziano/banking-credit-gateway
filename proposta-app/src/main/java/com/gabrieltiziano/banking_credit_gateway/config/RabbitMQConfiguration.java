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

    @Value("${rabbitmq.propostapendente.exchange}")
    private String exchangePropostaPendente;

    @Value("${rabbitmq.propostaconcluida.exchange}")
    private String exchangePropostaConcluida;

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
        return ExchangeBuilder.directExchange("dead-letter.ex").build();
    }

    // PROPOSTA PENDENTE | MS ANÁLISE CRÉDITO
    @Bean
    public Queue criarFilaPropostaPendenteMsAnaliseCredito(){
        return QueueBuilder.durable("proposta-pendente.ms-analise-credito")
                .deadLetterExchange("dead-letter.ex")
                .deadLetterRoutingKey("proposta-pendente.ms-analise-credito.dlq")
                .build();
    }

    @Bean
    public Binding criarBindingPropostaPendenteMSNotificacao(){
        return BindingBuilder
                .bind(criarFilaPropostaPendenteMsNotificacao())
                .to(criarFanoutExchangePropostaPendente());
    }

    @Bean
    public Queue criarDLQPropostaPendenteMsAnaliseCredito(){
        return QueueBuilder.durable("proposta-pendente.ms-analise-credito.dlq").build();
    }

    @Bean
    public Binding criarBindingDLQPropostaPendenteMsAnaliseCredito(){
        return BindingBuilder
                .bind(criarDLQPropostaPendenteMsAnaliseCredito())
                .to(criarDeadLetterExchange())
                .with("proposta-pendente.ms-analise-credito.dlq");
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
        return QueueBuilder.durable("proposta-pendente.ms-notificacao")
                .deadLetterExchange("dead-letter.ex")
                .deadLetterRoutingKey("proposta-pendente.ms-notificacao.dlq")
                .build();
    }

    @Bean
    public Queue criarDLQPropostaPendenteMsNotificacao(){
        return QueueBuilder.durable("proposta-pendente.ms-notificacao.dlq").build();
    }

    @Bean
    public Binding criarBindingDLQPropostaPendenteMsNotificacao(){
        return BindingBuilder
                .bind(criarDLQPropostaPendenteMsNotificacao())
                .to(criarDeadLetterExchange())
                .with("proposta-pendente.ms-notificacao.dlq");
    }

    // 3) PROPOSTA CONCLUÍDA | MS PROPOSTA
    @Bean
    public Queue criarFilaPropostaConcluidaMsProposta(){
        return QueueBuilder.durable("proposta-concluida.ms-proposta")
                .deadLetterExchange("dead-letter.ex")
                .deadLetterRoutingKey("proposta-concluida.ms-proposta.dlq")
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
        return QueueBuilder.durable("proposta-concluida.ms-proposta.dlq").build();
    }

    @Bean
    public Binding criarBindingDLQPropostaConcluidaMsProposta(){
        return BindingBuilder
                .bind(criarDLQPropostaConcluidaMsProposta())
                .to(criarDeadLetterExchange())
                .with("proposta-concluida.ms-proposta.dlq");
    }

    // 4) PROPOSTA CONCLUÍDA | MS NOTIFICAÇÃO
    @Bean
    public Queue criarFilaPropostaConcluidaMsNotificacao(){
        return QueueBuilder.durable("proposta-concluida.ms-notificacao")
                .deadLetterExchange("dead-letter.ex")
                .deadLetterRoutingKey("proposta-concluida.ms-notificacao.dlq")
                .build();
    }

    @Bean
    public Binding criarBindingPropostaConcluidaMSNotificacao(){
        return BindingBuilder
                .bind(criarFilaPropostaConcluidaMsNotificacao())
                .to(criarFanoutExchangePropostaConcluida());
    }

    @Bean
    public Queue criarDLQPropostaConcluidaMsNotificacao(){
        return QueueBuilder.durable("proposta-concluida.ms-notificacao.dlq").build();
    }

    @Bean
    public Binding criarBindingDLQPropostaConcluidaMsNotificacao(){
        return BindingBuilder
                .bind(criarDLQPropostaConcluidaMsNotificacao())
                .to(criarDeadLetterExchange())
                .with("proposta-concluida.ms-notificacao.dlq");
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
