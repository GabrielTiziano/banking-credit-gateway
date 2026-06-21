package com.gabrieltiziano.banking_credit_gateway.service;

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
                           @Value("${rabbitmq.propostapendente.exchange}") String exchange) {
        this.propostaRepository = propostaRepository;
        this.notificacaoService = notificacaoService;
        this.exchange = exchange;
    }

    @Transactional
    public PropostaResponse criar(PropostaRequest request) {
        Proposta propostaSalva = propostaRepository.save(PropostaMapper.toProposta(request));
        PropostaResponse dto = PropostaResponse.from(propostaSalva);
        notificacaoService.notificar(dto, exchange);
        return dto;
    }

    public List<PropostaResponse> listarPropostas(){
        return propostaRepository.findAll().stream().map(PropostaResponse::from).toList();
    }
}
