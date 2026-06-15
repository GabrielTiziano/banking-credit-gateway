package com.gabrieltiziano.banking_credit_gateway.service;

import com.gabrieltiziano.banking_credit_gateway.dto.PropostaRequest;
import com.gabrieltiziano.banking_credit_gateway.dto.PropostaResponse;
import com.gabrieltiziano.banking_credit_gateway.mapper.PropostaMapper;
import com.gabrieltiziano.banking_credit_gateway.repository.PropostaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PropostaService {
    private final PropostaRepository propostaRepository;

    public PropostaService(PropostaRepository propostaRepository) {
        this.propostaRepository = propostaRepository;
    }

    @Transactional
    public PropostaResponse criar(PropostaRequest request) {
        return PropostaResponse.from(propostaRepository.save(PropostaMapper.toProposta(request)));
    }
}
