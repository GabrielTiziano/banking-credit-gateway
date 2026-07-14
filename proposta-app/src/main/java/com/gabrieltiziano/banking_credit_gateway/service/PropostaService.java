package com.gabrieltiziano.banking_credit_gateway.service;

import com.gabrieltiziano.banking_credit_gateway.dto.PropostaPendenteMessage;
import com.gabrieltiziano.banking_credit_gateway.dto.PropostaRequest;
import com.gabrieltiziano.banking_credit_gateway.dto.PropostaResponse;
import com.gabrieltiziano.banking_credit_gateway.entities.Proposta;
import com.gabrieltiziano.banking_credit_gateway.mapper.PropostaMapper;
import com.gabrieltiziano.banking_credit_gateway.repository.PropostaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PropostaService {
    private final PropostaRepository propostaRepository;
    private final NotificacaoService notificacaoService;

    private final String exchange;

    public PropostaService(PropostaRepository propostaRepository,
                           NotificacaoService notificacaoService,
                           @Value("${rabbitmq.exchange.proposta-pendente}") String exchange) {
        this.propostaRepository = propostaRepository;
        this.notificacaoService = notificacaoService;
        this.exchange = exchange;
    }

    @Transactional
    public PropostaResponse criar(PropostaRequest request) {
        Proposta proposta = PropostaMapper.toProposta(request);
        proposta.setIntegrada(true);
        Proposta salva = propostaRepository.save(proposta);
        notificar(salva);
        return PropostaResponse.from(salva);
    }

    private void notificar(Proposta proposta){
        try {
            notificacaoService.notificar(PropostaPendenteMessage.from(proposta), exchange);
        } catch (Exception e) {
            proposta.setIntegrada(false);
            propostaRepository.save(proposta);
        }
    }

    public List<PropostaResponse> listarPropostas(){
        return propostaRepository.findAll().stream().map(PropostaResponse::from).toList();
    }
}